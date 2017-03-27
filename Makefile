git_version = $$(git branch 2>/dev/null | sed -e '/^[^*]/d'-e's/* \(.*\)/\1/')
npm_bin= $$(npm bin)

all: test-pc-browser
install:
	npm install
test-pc-browser: server
	mvn -s settings.xml clean install -Dmaven.test.skip=true
	mvn test -Dtest=DesktopSampleTest
travis-pc-browser: install server
	npm install macaca-electron --save-dev
	${npm_bin}/macaca doctor
	sleep 10s
	mvn -s settings.xml clean install -Dmaven.test.skip=true
	mvn test -Dtest=DesktopSampleTest
travis-android: install server
	npm install macaca-android --save-dev
	${npm_bin}/macaca doctor
	sleep 10s
	mvn -s settings.xml clean install -Dmaven.test.skip=true
	mvn test -Dtest=AndroidSampleTest
travis-ios: install server
	npm install macaca-ios --save-dev
	${npm_bin}/macaca doctor
	mvn -s settings.xml clean install -Dmaven.test.skip=true
	mvn test -Dtest=IosSampleTest
travis-android-browser: install server
	npm install macaca-android --save-dev
	${npm_bin}/macaca doctor
	mvn -s settings.xml clean install -Dmaven.test.skip=true
	mvn test -Dtest=H5SampleTest
server:
	${npm_bin}/macaca server --verbose &
	sleep 10s
.PHONY: test
