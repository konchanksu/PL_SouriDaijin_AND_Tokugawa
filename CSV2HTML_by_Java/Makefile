ANT	= env LC_ALL=ja_JP.UTF-8 ant
ARCHIVE	= $(shell basename `pwd`)
PMD_BIN = "pmd-bin"
PMD_BASE_DIR = "pmd"
PMD_RULES = "pmd-rules.xml"
PMD_COMMAND = ./$(PMD_BIN)/run.sh pmd -d ./ -f textcolor -R ./$(PMD_BASE_DIR)/${PMD_RULES} -l java
PMD_COMMAND_HTML	= ./$(PMD_BIN)/run.sh pmd -d ./ -f html -R ./$(PMD_BASE_DIR)/${PMD_RULES}  -l java
PMD_REPORT_HTML	= pmd_report.html

all:
	$(ANT) all

clean:
	$(ANT) clean
	@find . -maxdepth 1 -name "$(PMD_REPORT_HTML)" -exec echo rm -f {} ";" -exec rm -f {} ";"

test:
	$(ANT) test

install:
	$(ANT) install

#pmdの準備
pmd_prepare:all
	sh ./PreparePMD.sh

#PMDを実行
pmd:pmd_prepare
	@echo $(PMD_COMMAND)
	@eval $(PMD_COMMAND) 2> /dev/null

#PMDのHTML出力
pmd_html:pmd_prepare
	@echo $(PMD_COMMAND_HTML) '> ./$(PMD_REPORT_HTML)'
	-@eval $(PMD_COMMAND_HTML) > ./$(PMD_REPORT_HTML) 2> /dev/null
	open -a 'Safari' ./$(PMD_REPORT_HTML)

#JUnitの実行
unittest:
	$(ANT) unittest

doc:
	$(ANT) doc

#PMD関連のディレクトリ・フォルダを完全に削除
wipe:clean
	@read -p "本当に削除しますか? (y/n): " INPUT;if [ "$${INPUT}" != 'y' ];then echo process stopped;exit 1;fi;
	@find . -name ".DS_Store" -exec echo rm -f {} ";" -exec rm -f {} ";"
	@readlink pmd-bin | xargs -I{} echo `pwd`/{} | xargs dirname | xargs -I{} sh -c 'rm -rf {}; echo rm -rf {}'
	@find . -maxdepth 1 -name "$(PMD_BIN)" -exec echo rm -f {} ";" -exec rm -f {} ";"

#zip:
#	$(ANT) zip
zip: clean
	(cd ../ ; zip -r ./$(ARCHIVE).zip ./$(ARCHIVE)/)
