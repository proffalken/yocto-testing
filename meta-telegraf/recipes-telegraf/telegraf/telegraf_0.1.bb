SUMMARY = "The plugin-driven server agent for collecting & reporting metrics."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/${PN}-${PV}-LICENSE;md5=96cd9a86f733dbfec4107613b9b27c71"

SRC_URI = " \
    https://raw.githubusercontent.com/influxdata/${PN}/${PV}/LICENSE;downloadfilename=${PN}-${PV}-LICENSE;name=license \
"
SRC_URI_append_arm = "\
    https://dl.influxdata.com/telegraf/releases/telegraf-${PV}_linux_armhf.tar.gz;name=armhf \
"
SRC_URI[license.md5sum] = "96cd9a86f733dbfec4107613b9b27c71"
SRC_URI[armhf.sha256sum] = "21c846f08ffa84830546688901a3eaf9987197a703a0092796e13079481ac8c9"

COMPATIBLE_HOST = "arm"
COMPATIBLE_HOST_armv4 = "null"
COMPATIBLE_HOST_armv5 = "null"
COMPATIBLE_HOST_armv6 = "null"

S = "${WORKDIR}/telegraf"

inherit bin_package distro_features_check systemd
REQUIRED_DISTRO_FEATURES = "systemd"

do_install_append() {
    install -d ${D}${systemd_system_unitdir}
    sed -e '/User=telegraf/d; /\[Unit\]/aConditionPathExists=/etc/telegraf/telegraf.conf' <${D}/usr/lib/telegraf/scripts/telegraf.service >${D}${systemd_system_unitdir}/telegraf.service
    rm -r ${D}/var ${D}/etc/logrotate.d ${D}/usr/lib/telegraf \
        ${D}/etc/telegraf/telegraf.conf
}

INSANE_SKIP_${PN} = "already-stripped"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"
SYSTEMD_AUTO_ENABLE = "enable"
