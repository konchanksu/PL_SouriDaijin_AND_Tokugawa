#!/bin/sh

: <<EOF_COMMENT
    PMDをダウンロード・展開し、PMDのbinディレクトリのシンボリックリンクを作成する.
    また、pmd-rulesが存在しない場合はそれもダウンロードする
EOF_COMMENT

#これらはMakefileと文字列を合わせる
PMD_BIN="pmd-bin"
PMD_BASE_DIR="pmd"
PMD_RULES="pmd-rules.xml"

PMD_VERSION="6.40.0"
PMD_DIR="pmd-bin-${PMD_VERSION}"
PMD_URL="https://github.com/pmd/pmd/releases/download/pmd_releases%2F${PMD_VERSION}/${PMD_DIR}.zip"
#下のURLは将来的にこのプロジェクトのリポジトリのxmlを参照するかも
PMD_RULES_URL="https://raw.githubusercontent.com/pmd/pmd/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml"

if [ -e ${PMD_BASE_DIR} ];then
    if [ ! -f "${PMD_BASE_DIR}/${PMD_RULES}" ];then
        curl -L ${PMD_RULES_URL} -o "./${PMD_BASE_DIR}/${PMD_RULES}"
    fi

    if [ -d "${PMD_BASE_DIR}/${PMD_DIR}" ];then
        ln -s "${PMD_BASE_DIR}/${PMD_DIR}/bin" ${PMD_BIN}
        exit 0
    fi
    rm -rf ${PMD_BASE_DIR}
fi

mkdir ${PMD_BASE_DIR}
curl -L ${PMD_RULES_URL} -o "./${PMD_BASE_DIR}/${PMD_RULES}"
curl -OL ${PMD_URL}
unzip ${PMD_DIR}.zip -d "${PMD_BASE_DIR}/"
rm ${PMD_DIR}.zip
ln -s "${PMD_BASE_DIR}/${PMD_DIR}/bin" ${PMD_BIN}

exit 0
