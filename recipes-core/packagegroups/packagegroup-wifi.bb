DESCRIPTION = "Package group WiFi"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = " \
  packagegroup-base-wifi \
  rtl8723bs-mp \
  dhcp-client \
  crda \
"
