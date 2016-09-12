DESCRIPTION = "CHIP image with WiFi tools to connect to a WiFi network"
LICENSE = "MIT"

require recipes-core/images/chip-image-minimal.bb

IMAGE_INSTALL += " \
  chip-packagegroup-wifi \
"
