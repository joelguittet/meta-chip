DESCRIPTION = "CHIP image with WiFi tools to create a WiFi hotspot"
LICENSE = "MIT"

require recipes-core/images/chip-image-minimal.bb

IMAGE_INSTALL += " \
  packagegroup-wifi-hotspot \
"
