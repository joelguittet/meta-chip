DESCRIPTION = "Package group WiFi"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN}_chip = " \
  packagegroup-base-wifi \
  rtl8723bs \
  dhcp-client \
  crda \
"

RDEPENDS_${PN}_chip-pro = " \
  packagegroup-base-wifi \
  rtl8723ds \
  dhcp-client \
  crda \
"
