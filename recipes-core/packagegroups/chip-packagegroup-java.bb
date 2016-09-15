DESCRIPTION = "Package group JAVA"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = " \
  openjdk-7-jre \
"
