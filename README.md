meta-chip
==

NextThingCo C.H.I.P. Yocto meta layer.

This layer contains kernel, u-boot and image recipes to flash the NextThingCo C.H.I.P. board.

This layer depends on the additional layers:
* meta-yocto
* meta-yocto-bsp
* meta-openembedded/meta-oe
* meta-openembedded/meta-python
* meta-openembedded/meta-networking
* meta-java (from http://git.yoctoproject.org/cgit/cgit.cgi/meta-java - only if building openjdk)


Images
--

The following images are available:
* chip-image-minimal: the minimal image which is used to get the hardware running. It has USB Gagdet with console over USB OTG port feature. Images all require this image.
* chip-image-wifi: image with WiFi tools to connect to an external network.
* chip-image-wifi-hotspot: image with WiFi tools to create an hotspot.

The wanted image is chosen during the build with bitbake command.

New images created in other layers should at least require chip-image-minimal. 


Package groups
--

The following package groups are available:
* chip-packagegroup-wifi: to build images with WiFi tools to connect to an external network.
* chip-packagegroup-wifi-hotspot: to build images with WiFi tools to create an hotspot.
* chip-packagegroup-java: to build images with OpenJdk to execute Java applications.
* chip-packagegroup-java-dio: to build images with OpenJdk and OpenJdk-DIO project used to access hardware from Java applications.

Package groups are included in wanted images.


Using
--

The following tutorial is useful to start building your own Yocto project and loading C.H.I.P.

**_1- Install System Dependencies (once)_**

	sudo apt-get update && sudo apt-get upgrade
	sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat libsdl1.2-dev xterm lzop u-boot-tools git build-essential curl libusb-1.0-0-dev python-pip minicom
	sudo pip install --upgrade pip && sudo pip install pyserial

**_2- Get sources and flashing tools (once)_**

Clone sources:

	git clone --branch fido git://git.yoctoproject.org/poky.git ~/yocto/poky
	git clone --branch fido git://git.openembedded.org/meta-openembedded ~/yocto/meta-openembedded
	git clone https://github.com/myfreescalewebpage/meta-chip.git ~/yocto/meta-chip

Get and build tools:

	git clone http://github.com/NextThingCo/sunxi-tools ~/yocto/sunxi-tools
	cd ~/yocto/sunxi-tools
	make
	rm /usr/local/bin/fel
	sudo ln -s $PWD/fel /usr/local/bin/fel
	git clone --branch yocto https://github.com/myfreescalewebpage/CHIP-tools ~/yocto/chip-tools
	mkdir -p ~/yocto/images

**_3- Configure build (once)_**

Setup environnement:

	cd ~/yocto
	source poky/oe-init-build-env

Add layers to the configuration file ~/yocto/build/conf/bblayers.conf:

	BBLAYERS ?= " \
	  ${TOPDIR}/../poky/meta \
	  ${TOPDIR}/../poky/meta-yocto \
	  ${TOPDIR}/../poky/meta-yocto-bsp \
	  ${TOPDIR}/../meta-openembedded/meta-oe \
	  ${TOPDIR}/../meta-openembedded/meta-python \
	  ${TOPDIR}/../meta-openembedded/meta-networking \
	  ${TOPDIR}/../meta-chip \
	"

Set machine in the configuration file ~/yocto/build/conf/local.conf:

	MACHINE ??= "chip"

**_4- Restore environnement (when restarting the development machine)_**

Restore environnement:

        cd ~/yocto
        source poky/oe-init-build-env

**_5- Build_**

Build minimal image and u-boot:

	cd ~/yocto/build
	bitbake chip-image-minimal

**_6- Flash target_**

Copy files in the images directory and flash the target (replace chip-image-minimal-chip.ubi by the wanted rootfs if you have build another image):

	cp ~/yocto/build/tmp/deploy/images/chip/chip-image-minimal-chip.ubi ~/yocto/images/rootfs.ubi
	cp ~/yocto/build/tmp/deploy/images/chip/sunxi-spl.bin ~/yocto/images
	cp ~/yocto/build/tmp/deploy/images/chip/sunxi-spl-with-ecc.bin ~/yocto/images
	cp ~/yocto/build/tmp/deploy/images/chip/u-boot-dtb.bin ~/yocto/images
	cd ~/yocto/chip-tools/
	sudo BUILDROOT_OUTPUT_DIR=~/yocto ./chip-fel-flash.sh

Then start the target in FEL mode (put a jumper between the FEL pin and GND and then power ON). Logs are displayed to check the progression and the verification of the flashing procedure.
At the end of the flashing procedure, the target is powered off. Disconnect the power supply and remove the FEL jumper. Restart the target. A console is available on the UART pins of the board and another one on the USB OTG cable (you should see a new tty device when connecting C.H.I.P. to your computer). Speed is 115200 for both consoles. Login is 'root' with no password.


Contributing
--

All contributions are welcome :-)

Use Github Issues to report anomalies or to propose enhancements (labels are available to clearly identify what you are writing) and Pull Requests to submit modifications.


References
--

* https://github.com/agherzan/meta-chip
* https://github.com/soderstrom-rikard/meta-sunxi/tree/sun5i-r8-chip
* https://bbs.nextthing.co/t/yocto-project-an-initial-teaser-release/833
* https://bbs.nextthing.co/t/flash-chip-using-ubuntu-on-usb/2401

Special thanks to Rikard Soderstrom who integrated C.H.I.P. in the meta-sunxi layer.
