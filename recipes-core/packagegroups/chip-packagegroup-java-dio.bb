DESCRIPTION = "Package group JAVA with OpenJDK DIO"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = " \
  chip-packagegroup-java \
  openjdk-dio \
"
