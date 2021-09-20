require './fhir_client'
require 'gpgme'

class QuestionnaireResponseClient

  def initialize
    load_key
  end

  def get_questionnaire_response(id)
    FhirClient.new

    response = FHIR::QuestionnaireResponse.read(id)

    decrypt(response) if is_encrypted_response(response)

    response
  end

  def search_questionnaire_response
    FhirClient.new

    params = {
      'based-on': '97f680b9-e397-4298-8c53-de62a284c806',
      'patient.identifier': '6226217e'
    }
    FHIR::QuestionnaireResponse.search(params)
  end

  private

  def load_key
    GPGME::Key.import(File.open('../../sandbox.key'))
  end

  def is_encrypted_response(response)
    response.meta.profile.any? { |profile| profile == 'http://openhealthhub.com/fhir/StructureDefinition/EncryptedQuestionnaireResponse' }
  end

  def decrypt(response)
    extensions = response.extension.select { |extension| extension.url == 'http://openhealthhub.com/fhir/StructureDefinition/encryptedAnswers' }
    crypto = GPGME::Crypto.new
    encrypted_answers = fix_encrypted_message_for_xml_response(extensions)
    decrypted_string = crypto.decrypt(encrypted_answers, password: 'api-sandbox').to_s
    decrypted_answers = JSON.parse(decrypted_string)
    add_to_response(response, decrypted_answers)
  end

  # Encrypted message is not correctly parsed in XML mode
  def fix_encrypted_message_for_xml_response(extensions)
    new_line_fix = extensions.first.valueString.gsub(/.*\s{2}/, '').gsub(' -----END PGP MESSAGE-----', '').gsub(' ', "\n")
    "-----BEGIN PGP MESSAGE-----\n\n" + new_line_fix + "\n-----END PGP MESSAGE-----"
  end

  def add_to_response(response, decrypted_answers)
    response.item.each { |item| handle_item(item, response, decrypted_answers) }
  end

  def handle_item(item, response, decrypted_answers)
    set_decrypted_answer(item.answer, decrypted_answers[item.linkId]) if decrypted_answers.keys.include?(item.linkId)

    item.item.each { |nested_item| handle_item(nested_item, response, decrypted_answers) }
  end

  def set_decrypted_answer(answers, decrypted_value)
    answers.each_with_index do |answer, i|
      # Workaround for https://github.com/fhir-crucible/fhir_models/issues/93
      is_encrypted_string = !answer.valueString.nil? && answer.valueString['extension']['url'] == 'http://openhealthhub.com/fhir/StructureDefinition/encrypted-stringType'
      answer.valueString = decrypted_value[i] if is_encrypted_string

      is_encrypted_coding = !answer.valueCoding.nil? && answer.valueCoding.extension[0].url == 'http://openhealthhub.com/fhir/StructureDefinition/encrypted-coding'
      answer.valueCoding.code = decrypted_value[i] if is_encrypted_coding

      # Workaround for https://github.com/fhir-crucible/fhir_models/issues/93
      is_encrypted_decimal = !answer.valueDecimal.nil? && answer.valueDecimal['extension']['url'] == 'http://openhealthhub.com/fhir/StructureDefinition/encrypted-decimalType'
      answer.valueDecimal = decrypted_value[i] if is_encrypted_decimal

      is_encrypted_attachment = !answer.valueAttachment.nil? && answer.valueAttachment.extension[0].url == 'http://openhealthhub.com/fhir/StructureDefinition/encrypted-attachment'
      answer.valueAttachment.data = decrypted_value[i] if is_encrypted_attachment

      # Workaround for https://github.com/fhir-crucible/fhir_models/issues/93
      is_encrypted_date = !answer.valueDate.nil? && answer.valueDate['extension']['url'] == 'http://openhealthhub.com/fhir/StructureDefinition/encrypted-dateType'
      answer.valueDate = decrypted_value[i] if is_encrypted_date
    end
  end

end

