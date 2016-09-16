SUMMARY = "OpenJDK DIO"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=dd439af477363980bb4b16ba062876cc"

DEPENDS_${PN} = "openjdk-7-jre"
RDEPENDS_${PN} = "openjdk-7-jre"

PR = "r0"

SRC_URI = " \
  hg://hg.openjdk.java.net/dio;module=master;rev=2f7da76bdc69;protocol=http \
  file://Makefile.patch \
"

S = "${WORKDIR}/master"

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg ${PN}-staticdev"

export JAVA_HOME="${STAGING_DIR_NATIVE}/usr/lib/jvm/icedtea7-native"

do_compile () {
  oe_runmake osgi_build_arm
}

do_install_append () {
  install -d ${D}/lib
  install -d ${D}${ROOT_HOME}
  install -d ${D}/usr/share/java
  install ${WORKDIR}/master/build/so/libdio.so ${D}/lib/libdio.so
  install -m 0644 ${WORKDIR}/master/test/java.policy ${D}${ROOT_HOME}/java.policy
  install -m 0644 ${WORKDIR}/master/build/jar/dio.jar ${D}/usr/share/java/dio.jar
}

FILES_${PN} = " \
  /lib/libdio.so \
  ${ROOT_HOME}/java.policy \
  /usr/share/java/dio.jar \
"
