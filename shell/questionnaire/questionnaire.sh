#!/bin/bash

source ../config.sh

printf "Getting Questionnaire\n\n"

curl "$baseUrl/Questionnaire/1"

