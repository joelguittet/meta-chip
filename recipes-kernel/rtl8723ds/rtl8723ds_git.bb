DESCRIPTION = "RTL8723DS SDIO WiFi Driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://core/rtw_bt_mp.c;md5=05099f911f5c256187a1236fe262ea34"

COMPATIBLE_MACHINE = "chip-pro"

inherit module

SRCREV = "0d60107a4c0bdedf8cf7858f717226028501e6b5"

SRC_URI = " \
    git://github.com/joelguittet/rtl8723ds.git;protocol=git;branch=master \
    file://Makefile.patch \
"

S = "${WORKDIR}/git"

# Custom Make options
RTL8723DS_MAKE_FLAGS += " \
    -DCONFIG_IOCTL_CFG80211 \
    -DRTW_USE_CFG80211_STA_EVENT \
    -DCONFIG_CONCURRENT_MODE \
    -DCONFIG_AUTO_AP_MODE \
    -DCONFIG_MP_INCLUDED \
    -DCONFIG_WIFI_MONITOR \
"

EXTRA_OEMAKE += " \
    USER_EXTRA_CFLAGS='${RTL8723DS_MAKE_FLAGS}' \
    KSRC=${STAGING_KERNEL_DIR} \
    PREFIX=${D} \
"
