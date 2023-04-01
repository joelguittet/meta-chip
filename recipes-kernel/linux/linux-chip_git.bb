DESCRIPTION = "Linux Kernel for C.H.I.P. boards"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "chip|chip-pro"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

SRCREV ?= "bce5de1cdc3667a2bc454219a0856b4f415b33f5"

SRC_URI += " \
    git://github.com/joelguittet/chip-linux.git;protocol=git;branch=nextthing/4.4/chip \
    file://defconfig \
"

LINUX_VERSION ?= "4.4"
PV = "${LINUX_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

do_install_append() {
    # Workaround for bug in fido, ocurrs when building out-of-tree modules
    # Ref: https://lists.yoctoproject.org/pipermail/yocto/2015-May/024738.html
    cp -f ${KBUILD_OUTPUT}/Module.symvers ${STAGING_KERNEL_BUILDDIR}
}

# Automatically depend on lzop-native if CONFIG_KERNEL_LZO is enabled
python () {
    try:
        defconfig = bb.fetch2.localpath('file://defconfig', d)
    except bb.fetch2.FetchError:
        return

    try:
        configfile = open(defconfig)
    except IOError:
        return

    if 'CONFIG_KERNEL_LZO=y\n' in configfile.readlines():
        depends = d.getVar('DEPENDS', False)
        d.setVar('DEPENDS', depends + ' lzop-native')
}
