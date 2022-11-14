<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRBackboneElement\FHIRQuestionnaireResponse\FHIRQuestionnaireResponseAnswer;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRBackboneElement\FHIRQuestionnaireResponse\FHIRQuestionnaireResponseItem;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRCoding;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRBundle;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRQuestionnaireResponse;
use gnupg;

class QuestionnaireResponseClient
{
    public function getQuestionnaireResponse($id): FHIRQuestionnaireResponse
    {
        $client = new FhirClient();
        $res = $client->get(sprintf('QuestionnaireResponse/%s', $id));
        return $this->decrypt(new FHIRQuestionnaireResponse($res));
    }

    public function searchQuestionnaireResponses($planDefReference): FHIRBundle
    {
        $client = new FhirClient();
        $res = $client->search(sprintf('QuestionnaireResponse?based-on.instantiates-canonical=%s', $planDefReference));
        return new FHIRBundle($res);
    }

    private function decrypt(FHIRQuestionnaireResponse $resp): FHIRQuestionnaireResponse
    {
        $gnupg = $this->loadDecryptedPrivateKey();

        if ($this->isEncryptedResponse($resp)) {
            $decodedValues = json_decode($this->decryptValue($gnupg, $this->getEncryptedValue($resp)), true);
            $this->addToResponse($resp, $decodedValues);
        }

        return $resp;
    }

    private function loadDecryptedPrivateKey()
    {
        $keyASCII = file_get_contents(dirname(__FILE__) . '/../../../../sandbox.key');
        $gnupg = new gnupg();
        $importKeyResult = $gnupg->import($keyASCII);
        $fingerPrint = $importKeyResult['fingerprint'];
        $gnupg->adddecryptkey($fingerPrint, 'api-sandbox');
        return $gnupg;
    }

    private function decryptValue($gnupg, $value): string
    {
        $decrypt = $gnupg->decrypt($value);
        return $decrypt;
    }

    private function addToResponse(FHIRQuestionnaireResponse $resp, $decodedValues)
    {
        foreach ($resp->getItem() as $item) {
            $this->updateItemValue($decodedValues, $item);
        }
    }

    private function updateItemValue($decodedValues, FHIRQuestionnaireResponseItem $item): void
    {
        $linkId = $item->getLinkId()->getValue()->getValue();
        if (isset($decodedValues[$linkId])) {
            $values = $decodedValues[$linkId];
            $answers = $item->getAnswer();
            for ($i = 0; $i < count($values); $i++) {
                $this->setValue($item, $answers[$i], $values[$i]);
            }
        }
        foreach ($item->getItem() as $nestedItem) {
            $this->updateItemValue($decodedValues, $nestedItem);
        }
    }

    private function setValue(FHIRQuestionnaireResponseItem $item, FHIRQuestionnaireResponseAnswer $answer, $decryptedValue)
    {
        $value = $decryptedValue['value'];
        $valueString = $answer->getValueString();
        if (isset($valueString)) {
            $answer->setValueString($value);
        }

        $decimalValue = $answer->getValueDecimal();
        if (isset($decimalValue)) {
            $answer->setValueDecimal($value);
        }

        $dateValue = $answer->getValueDate();
        if (isset($dateValue)) {
            $answer->setValueDate($value);
        }

        $codingValue = $answer->getValueCoding();
        if (isset($codingValue)) {
            $answer->getValueCoding()->setCode($value);
        }

        $attachmentValue = $answer->getValueAttachment();
        if (isset($attachmentValue)) {
            $answer->getValueAttachment()->setData($value);
        }

        foreach ($decryptedValue['codes'] as $code) {
            $newCodeAnswer = new FHIRQuestionnaireResponseAnswer();
            $coding = new FHIRCoding();
            $coding->setCode($code['code']);
            $coding->setDisplay($code['display'] ?? null);
            $coding->setSystem($code['system']);
            $coding->setVersion($code['version'] ?? null);
            $newCodeAnswer->setValueCoding($coding);
            $item->addAnswer($newCodeAnswer);
        }
    }

    private function isEncryptedResponse(FHIRQuestionnaireResponse $resp)
    {
        foreach ($resp->getMeta()->getProfile() as $profile) {
            if ($profile->getValue() == 'http://openhealthhub.com/fhir/StructureDefinition/EncryptedQuestionnaireResponse') {
                return true;
            }
        }

        return false;
    }

    private function getEncryptedValue(FHIRQuestionnaireResponse $resp)
    {
        foreach ($resp->getExtension() as $extension) {
            if ($extension->getUrl() == 'http://openhealthhub.com/fhir/StructureDefinition/encryptedAnswers') {
                return $extension->getValueString();
            }
        }

        return null;
    }

}
