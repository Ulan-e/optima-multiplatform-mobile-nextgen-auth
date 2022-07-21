#!/usr/bin/env bash

function echoYell() {
    echo -e "\033[0;33m$1\033[0m"
}

function echoBlue() {
    echo -e "\033[0;34m$1\033[0m"
}

function echoRed() {
    echo -e "\033[0;31m$1\033[0m"
}

function cmdCleanIde() {
    rm -rf .idea
    find . -name \*.iml -type f -delete
}

function cmdLocalize() {
    if [[ ! -d "./sheets-localizations-generator" ]]
    then
        git clone git@gitlab.icerockdev.com:scl/sheets-localizations-generator.git
        cd sheets-localizations-generator
        npm install
    else
        cd sheets-localizations-generator
    fi

    npm start android strings "GSHEET_ID_HERE" 'platform!A1:C' ../android-app/src/main/res/
    npm start mpp strings "GSHEET_ID_HERE" 'mpp!A1:C' ../mpp-library/src/commonMain/resources/MR/
    npm start mpp plurals "GSHEET_ID_HERE" 'mpp-plurals!A1:D' ../mpp-library/src/commonMain/resources/MR/
    npm start ios strings "GSHEET_ID_HERE" 'platform!A1:C' ../ios-app/src/Resources/
}

function cmdHelp {
    echoYell 'Help'
    echoBlue 'Usage:'
    echoYell '  master.sh COMMAND <params>'
    echoYell ''
    echoBlue 'Commands:'
    echoYell '  help                            Помощь'
    echoYell '  clean_ide                       Удаление файлов конфигурации проекта IDEA'
    echoYell '  localize                        Сгенерировать файлы локализации из gSheets'
}

function run() {
    COMMAND=$1
    case "$COMMAND" in
        clean_ide)
            echoBlue 'Удаление конфигурационных файлов IDEA (для корректной работы проект должен быть закрыт)'
            cmdCleanIde
        ;;
        localize)
            cmdLocalize
        ;;
        help|*)
            cmdHelp
        ;;
    esac
}

run $@
