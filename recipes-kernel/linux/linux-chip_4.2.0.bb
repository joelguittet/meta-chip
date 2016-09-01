DESCRIPTION = "Linux Kernel for C.H.I.P. boards"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "chip"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.2-rc1"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV ?= "fd2ad2582c7fb4a5fedff5ac19ca37d138df3963"
SRC_URI += " \
    git://github.com/NextThingCo/CHIP-linux.git;protocol=git;branch=chip/stable \
    file://defconfig \
"

S = "${WORKDIR}/git"

do_install_append() {
    #workaround for bug in fido, ocurrs when building out-of-tree modules
    #ref: https://lists.yoctoproject.org/pipermail/yocto/2015-May/024738.html
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
