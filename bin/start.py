#!/usr/bin/env python
# -*- coding:utf-8 -*-

import sys
import os
import signal
import subprocess
import time
import re
import socket
import json
from optparse import OptionParser
from optparse import OptionGroup
from string import Template
import codecs
import platform

def printCopyright():
    print '''
YunQue (%s), From LarkMidTable !
LarkMidTable All Rights Reserved.

'''
    sys.stdout.flush()

if __name__ == "__main__":
	printCopyright()

