DESCRIPTION="U-Boot port for C.H.I.P boards"

require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "dtc-native"

LICENSE = "GPLv2"

LIC_FILES_CHKSUM = " \
    file://Licenses/Exceptions;md5=338a7cb1e52d0d1951f83e15319a3fe7 \
    file://Licenses/bsd-2-clause.txt;md5=6a31f076f5773aabd8ff86191ad6fdd5 \
    file://Licenses/bsd-3-clause.txt;md5=4a1190eac56a9db675d58ebe86eaf50c \
    file://Licenses/eCos-2.0.txt;md5=b338cb12196b5175acd3aa63b0a0805c \
    file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://Licenses/ibm-pibs.txt;md5=c49502a55e35e0a8a1dc271d944d6dba \
    file://Licenses/isc.txt;md5=ec65f921308235311f34b79d844587eb \
    file://Licenses/lgpl-2.0.txt;md5=5f30f0716dfdd0d91eb439ebec522ec2 \
    file://Licenses/lgpl-2.1.txt;md5=4fbd65380cdd255951079008b364516c \
"

COMPATIBLE_MACHINE = "chip|chip-pro"

DEFAULT_PREFERENCE_chip="1"

SRCREV = "c2d284fbba74083eed8ae853a10f665f6febfdf1"

SRC_URI = " \
    git://github.com/myfreescalewebpage/chip-u-boot.git;protocol=git;branch=nextthing/2016.01/next \
"

UBOOT_VERSION ?= "2016.01"
PV = "${UBOOT_VERSION}+git${SRCPV}"

PE = "1"

# sunxi-spl.bin
SUNXI_SPL_IMAGE = "sunxi-spl"
SUNXI_SPL_BINARY = "${SUNXI_SPL_IMAGE}.bin"

# u-boot-dtb.bin
UBOOT_DTB_IMAGE = "u-boot-dtb"
UBOOT_DTB_BINARY = "${UBOOT_DTB_IMAGE}.bin"

do_install_append() {

    # Install sunxi-spl
    install ${S}/spl/${SUNXI_SPL_BINARY} ${D}/boot/${SUNXI_SPL_IMAGE}-${PV}-${PR}
    ln -sf ${SUNXI_SPL_IMAGE}-${PV}-${PR} ${D}/boot/${SUNXI_SPL_BINARY}

    # Install u-boot-dtb
    install ${S}/${UBOOT_DTB_BINARY} ${D}/boot/${UBOOT_DTB_IMAGE}-${PV}-${PR}
    ln -sf ${UBOOT_DTB_IMAGE}-${PV}-${PR} ${D}/boot/${UBOOT_DTB_BINARY}
}

do_deploy_append() {

    # Deploy sunxi-spl
    install ${S}/spl/${SUNXI_SPL_BINARY} ${DEPLOYDIR}/${SUNXI_SPL_IMAGE}-${PV}-${PR}
    ln -sf ${SUNXI_SPL_IMAGE}-${PV}-${PR} ${DEPLOYDIR}/${SUNXI_SPL_BINARY}

    # Deploy u-boot-dtb
    install ${S}/${UBOOT_DTB_BINARY} ${DEPLOYDIR}/${UBOOT_DTB_IMAGE}-${PV}-${PR}
    ln -sf ${UBOOT_DTB_IMAGE}-${PV}-${PR} ${DEPLOYDIR}/${UBOOT_DTB_BINARY}
}
