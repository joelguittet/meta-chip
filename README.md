meta-chip
==

NextThingCo C.H.I.P. Yocto meta layer.

This layer contains kernel, u-boot and image recipes to flash the NextThingCo C.H.I.P. boards.

This layer depends on the additional mandatory layers:
* meta-yocto
* meta-yocto-bsp
* meta-openembedded/meta-oe
* meta-openembedded/meta-python
* meta-openembedded/meta-networking

Optionally, the following layers will be required:

* meta-java (from http://git.yoctoproject.org/cgit/cgit.cgi/meta-java - only if building openjdk)

Examples to use this layer are available in my Github at https://github.com/myfreescalewebpage/meta-chip-examples.


Philosophy of this meta layer
--

The main positions of this meta layer are the following:
* A single meta layer for all NextThingCo C.H.I.P. boards. Today, C.H.I.P. and C.H.I.P. PRO are both supported.
* The same baseline for all boards: same u-boot version (2016.01), the same kernel version (4.4), the same default kernel configuration. Only the specificities of the hardware differ (device tree and WiFi driver module).
* A step by step tutorial to help you building and flashing your first C.H.I.P. board (see chapter Using just below).
* Some simple tools to flash the boards (a single script to launch).

The whishes of the meta layer is to provide the most important abstraction to the hardware. Following the design rules described above, many applications can be executed on C.H.I.P. or C.H.I.P. PRO without to worry about the hardware version used in you final design.

Moreover, the meta layer is improved thinking to the impacts on your own meta layer and trying to reduce them to the minimum.


Images
--

The following images are available:
* chip-image-minimal: the minimal image which is used to get the hardware running. Images all require this image.

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

The following tutorial is useful to start building your own Yocto project and loading C.H.I.P. or C.H.I.P. PRO board. The development machine is running Ubuntu 16.04.

**_1- Install System Dependencies (once)_**

	sudo apt-get update && sudo apt-get upgrade
	sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat libsdl1.2-dev xterm lzop u-boot-tools git build-essential curl libusb-1.0-0-dev python-pip minicom
	sudo pip install --upgrade pip && sudo pip install pyserial

**_2- Get sources and flashing tools (once)_**

Clone sources:

	git clone --branch fido git://git.yoctoproject.org/poky.git ~/yocto/poky
	git clone --branch fido git://git.openembedded.org/meta-openembedded ~/yocto/meta-openembedded
	git clone https://github.com/myfreescalewebpage/meta-chip.git ~/yocto/meta-chip

Get and build sunxi tools:

	git clone http://github.com/linux-sunxi/sunxi-tools ~/yocto/sunxi-tools
	cd ~/yocto/sunxi-tools
	make
	make misc
	sudo make install
	sudo make install-misc

Get and build C.H.I.P. tools:

	git clone https://github.com/myfreescalewebpage/chip-tools ~/yocto/chip-tools

Update udev rules:

	echo -e 'SUBSYSTEM=="usb", ATTRS{idVendor}=="1f3a", ATTRS{idProduct}=="efe8", GROUP="plugdev", MODE="0660" SYMLINK+="usb-chip"
	SUBSYSTEM=="usb", ATTRS{idVendor}=="18d1", ATTRS{idProduct}=="1010", GROUP="plugdev", MODE="0660" SYMLINK+="usb-chip-fastboot"
	SUBSYSTEM=="usb", ATTRS{idVendor}=="1f3a", ATTRS{idProduct}=="1010", GROUP="plugdev", MODE="0660" SYMLINK+="usb-chip-fastboot"
	SUBSYSTEM=="usb", ATTRS{idVendor}=="067b", ATTRS{idProduct}=="2303", GROUP="plugdev", MODE="0660" SYMLINK+="usb-serial-adapter"
	' | sudo tee /etc/udev/rules.d/99-allwinner.rules
	sudo udevadm control --reload-rules

Create images directory:

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

Or:

	MACHINE ??= "chip-pro"

Depending of the expected target.

**_4- Restore environnement (when restarting the development machine)_**

Restore environnement:

        cd ~/yocto
        source poky/oe-init-build-env

**_5- Build_**

Build minimal image:

	cd ~/yocto/build
	bitbake chip-image-minimal

**_6- Flash target_**

### C.H.I.P.

Copy files in the images directory (replace chip-image-minimal-chip.ubi by the wanted rootfs if you have build another image):

	cp ~/yocto/build/tmp/deploy/images/chip/chip-image-minimal-chip.ubi ~/yocto/images/rootfs.ubi
	cp ~/yocto/build/tmp/deploy/images/chip/sunxi-spl.bin ~/yocto/images
	cp ~/yocto/build/tmp/deploy/images/chip/u-boot-dtb.bin ~/yocto/images

Then start the target in FEL mode (put a jumper between the FEL pin and GND and then power ON) as shown on the following image.

![FEL](http://flash.getchip.com/static/img/chipConnect.2c4d9dc.png)

Flash the target:

	cd ~/yocto/chip-tools/
	sudo ./chip-flash-chip.sh ~/yocto/images

Logs are displayed on the serial console interface (UART1) of the target to check the progression and the verification of the flashing procedure.

At the end of the flashing procedure, the target is running your image. Disconnect the power supply and remove the FEL jumper. Restart the target. The console is available on UART1 pins of the board and another one is also available throw the USB OTG cable (you should see a new tty device when connecting C.H.I.P. to your computer). Speed is 115200 for both consoles. Login is 'root' with no password.

### C.H.I.P. PRO

Copy files in the images directory (replace chip-image-minimal-chip-pro.ubi by the wanted rootfs if you have build another image):

	cp ~/yocto/build/tmp/deploy/images/chip-pro/chip-image-minimal-chip-pro.ubi ~/yocto/images/rootfs.ubi
	cp ~/yocto/build/tmp/deploy/images/chip-pro/sunxi-spl.bin ~/yocto/images
	cp ~/yocto/build/tmp/deploy/images/chip-pro/u-boot-dtb.bin ~/yocto/images

Then start the target in FEL mode (press FEL button while power ON) as shown on the following image.

![FEL](http://flash.getchip.com/static/img/pressPlugWhite.682b824.gif)

Flash the target:

	cd ~/yocto/chip-tools/
	sudo ./chip-flash-chip-pro.sh ~/yocto/images

Logs are displayed on the serial console interface (UART1) of the target to check the progression and the verification of the flashing procedure. Using the dev kit, UART1 console interface is available on the host throw an USB to Serial converter.

At the end of the flashing procedure, the target is running your image. The console is available on UART1 pins of the board and another one is also available throw the USB OTG cable (you should see a new tty device when connecting C.H.I.P. PRO to your computer). Speed is 115200 for both consoles. Login is 'root' with no password.


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
