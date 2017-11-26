#!/bin/bash

pkg install libllvm
pkg install make
apt install clang
cd Termux/termux-api-package/
make install