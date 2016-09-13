DESCRIPTION = "Package group JAVA"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PREFERRED_PROVIDER_virtual/java-initial = "cacao-initial"
PREFERRED_PROVIDER_virtual/java-native = "jamvm-native"
PREFERRED_PROVIDER_virtual/javac-native = "ecj-bootstrap-native"
PREFERRED_VERSION_openjdk-7-jre = "99b00-2.6.5"

RDEPENDS_${PN} = " \
  openjdk-7-jre \
"
