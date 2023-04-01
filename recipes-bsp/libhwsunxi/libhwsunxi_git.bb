SUMMARY = "libhwsunxi"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

PR = "r0"

SRCREV = "0e17ad3c7724a92806c2ec231d4412b73c34e2c8"

SRC_URI = " \
    git://github.com/joelguittet/libhwsunxi.git;protocol=git;branch=master \
"

S = "${WORKDIR}/git"

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg ${PN}-staticdev"

do_compile () {
    cd "${S}"
    ${CC} -c gpio.c -fPIC -o gpio.o
    ${CC} -c lradc.c -fPIC -o lradc.o
    ${CC} -c pwm.c -fPIC -o pwm.o
    ${CC} -c spi.c -fPIC -o spi.o
    ${CC} -shared -Wl,-soname,libhwsunxi.so -o libhwsunxi.so gpio.o lradc.o pwm.o spi.o
    ${AR} rcs libhwsunxi.a gpio.o lradc.o pwm.o spi.o
    ${RANLIB} libhwsunxi.a
}

do_install () {
    install -d ${D}${libdir}
    install libhwsunxi.so ${D}${libdir}
    install libhwsunxi.a ${D}${libdir}
    install -d ${D}${includedir}/libhwsunxi
    install *.h ${D}${includedir}/libhwsunxi
}

FILES_${PN} = " \
    ${libdir}/*.so \
"

FILES_${PN}-dev += " \
    ${includedir}/libhwsunxi/*.h \
"

FILES_${PN}-staticdev += " \
    ${libdir}/*.a \
"
