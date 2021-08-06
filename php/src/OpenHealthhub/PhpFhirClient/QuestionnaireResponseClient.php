<?php


namespace OpenHealthhub\PhpFhirClient;


use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRBackboneElement\FHIRQuestionnaireResponse\FHIRQuestionnaireResponseAnswer;
use DCarbone\PHPFHIRGenerated\R4\FHIRElement\FHIRBackboneElement\FHIRQuestionnaireResponse\FHIRQuestionnaireResponseItem;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRBundle;
use DCarbone\PHPFHIRGenerated\R4\FHIRResource\FHIRDomainResource\FHIRQuestionnaireResponse;
use OpenPGP;
use OpenPGP_Crypt_RSA;
use OpenPGP_Crypt_Symmetric;
use OpenPGP_Message;

class QuestionnaireResponseClient
{
    public function getQuestionnaireResponse($id): FHIRQuestionnaireResponse
    {
        $client = new FhirClient();
        $res = $client->get(sprintf('QuestionnaireResponse/%s', $id));
        return $this->decrypt(new FHIRQuestionnaireResponse($res));
    }

    public function searchQuestionnaireResponses($partOf, $identifier): FHIRBundle
    {
        $client = new FhirClient();
        $res = $client->search(sprintf('QuestionnaireResponse?part-of=%s&identifier=%s', $partOf, $identifier));
        return new FHIRBundle($res);
    }

    private function decrypt(FHIRQuestionnaireResponse $resp): FHIRQuestionnaireResponse
    {
        $keyDecrypted = $this->getDecryptedPrivateKey();

        if ($this->isEncryptedResponse($resp)) {
                $decodedValues = json_decode($this->decryptValue($this->getEncryptedValue($resp), $keyDecrypted));
                $this->addToResponse($resp, $decodedValues);
        }

        return $resp;
    }

    private function getDecryptedPrivateKey()
    {
        // TODO OP-651 loading the private key fails on the Message::parse function, we could try with GnuPG, but can't get it to work on Windows
        $keyASCII = file_get_contents(dirname(__FILE__) . '/../../../../sandbox.key');
        $keyEncrypted = OpenPGP_Message::parse(OpenPGP::unarmor($keyASCII, 'PGP PRIVATE KEY BLOCK'));
        return OpenPGP_Crypt_Symmetric::decryptSecretKey('sandbox-api', $keyEncrypted[0]);
    }

    private function decryptValue($value, $keyDecrypted): ?OpenPGP_Message
    {
        $msg = OpenPGP_Message::parse(OpenPGP::unarmor($value, 'PGP MESSAGE'));

        $decryptor = new OpenPGP_Crypt_RSA($keyDecrypted);
        return $decryptor->decrypt($msg);
    }

    private function addToResponse(FHIRQuestionnaireResponse $resp, mixed $decodedValues)
    {
        foreach ($resp->getItem() as $item) {
            $this->updateItemValue($decodedValues, $item);
        }
    }

    private function updateItemValue(mixed $decodedValues, FHIRQuestionnaireResponseItem $item): void
    {
        $linkId = $item->getLinkId()->getValue()->getValue();
        if (isset($decodedValues[$linkId])) {
            $values = $decodedValues[$linkId];
            $answers = $item->getAnswer();
            for ($i = 0; $i < count($values); $i++) {
                $this->setValue($answers[$i], $values[$i]);
            }
        }
        foreach ($item->getItem() as $nestedItem) {
            $this->updateItemValue($decodedValues, $nestedItem);
        }
    }

    private function setValue(FHIRQuestionnaireResponseAnswer $answer, string $value)
    {
        $valueString = $answer->getValueString();
        if (isset($valueString)) {
            $answer->setValueString($value);
            return;
        }

        $decimalValue = $answer->getValueDecimal();
        if (isset($decimalValue)) {
            $answer->setValueDecimal($value);
            return;
        }

        $dateValue = $answer->getValueDate();
        if (isset($dateValue)) {
            $answer->setValueDate($value);
            return;
        }

        $codingValue = $answer->getValueCoding();
        if (isset($codingValue)) {
            $answer->getValueCoding()->setCode($value);
            return;
        }

        $attachmentValue = $answer->getValueAttachment();
        if (isset($attachmentValue)) {
            $answer->getValueAttachment()->setData($value);
        }
    }

    private function isEncryptedResponse(FHIRQuestionnaireResponse $resp)
    {
        foreach ($resp->getMeta()->getProfile() as $profile) {
            if ($profile->getValue() == 'http://openhealthhub.com/StructureDefinition/EncryptedQuestionnaireResponse') {
                return true;
            }
        }

        return false;
    }

    private function getEncryptedValue(FHIRQuestionnaireResponse $resp)
    {
        foreach ($resp->getExtension() as $extension) {
            if ($extension->getUrl() == 'http://openhealthhub.com/StructureDefinition/encryptedAnswers') {
                return $extension->getValueString();
            }
        }

        return null;
    }

}
