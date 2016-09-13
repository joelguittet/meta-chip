SUMMARY = "OpenJDK DIO"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=dd439af477363980bb4b16ba062876cc"

RDEPENDS_${PN} = "openjdk-7-jre"

PR = "r0"

SRC_URI = " \
  hg://hg.openjdk.java.net/dio;module=master;rev=2f7da76bdc69;protocol=http \
  file://Makefile.patch \
"

S = "${WORKDIR}/master"

export JAVA_HOME="${STAGING_DIR_NATIVE}/usr/lib/jvm/icedtea7-native"

do_compile () {
  oe_runmake osgi_build_arm
}

#do_install_append () {
#  install -d ${D}${ROOT_HOME}
#  install -m 0644 ${WORKDIR}/helloworld-${PV}.jar ${D}${ROOT_HOME}/helloworld.jar
#}
#
#FILES_${PN} = " \
#  ${ROOT_HOME}/helloworld.jar \
#"
