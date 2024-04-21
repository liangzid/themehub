"""
======================================================================
DATA ---

Data of Themes.

    Author: Zi Liang <zi1415926.liang@connect.polyu.hk>
    Copyright © 2024, ZiLiang, all rights reserved.
    Created: 21 April 2024
======================================================================
"""


# ------------------------ Code --------------------------------------

## normal import 
import json
from typing import List,Tuple,Dict
import random
from pprint import pprint as ppp



theme_dict={}


transformer={
     "blue":  "#C2E8F7",
     "red":  "#FCE0E1",
     "yellow":  "#F2F4C1",
     "orange":  "#FFE2BB",
     "green":  "#CCE7CF",
     "grey-dark":  "#DBDFEF",
     "grey-white":  "#F3F3F4",
     "purple":  "#C5BEDF"
    }

lake={
    "deep1":"#1B262C",
    "pad-7":"#144272",
    "pad-8":"#2C74B3",
    "deep2":"#0F4C75",
    "deep3":"#3282B8",
    "deep4":"#BBE1FA",
    "deep5":"#6096B4",
    "deep6":"#93BFCF",

    }
  
fill_between={
    "vanilla1": "#4a148c",
    "vanilla2": "#469de9",
    "our1": "#eb3b5a",
    "vanilla3": "#3867d6",

    "1": "#9c27b0",
    "2": "#98c8f3",
    "3": "#f78fb3",
    "4": "#778beb",
    }
                   
PEA_contrast={
    "c1":"#96c0ea",
    "c2":"#6ca7e2",
    "c3":"#428eda",
    "c4":"#fba1a9",
    "c5":"#ff7785",
    "c6":"#ff0a22",

    "pad-7":"#ffffff",
    "pad-8":"#ffffff",
    }

yellow={
    "5":"#f3ece9",
    "6":"#bdb5a6",
    "1":"#d4c9b5",
    "2":"#aba290",
    "3":"#7a7262",
    "4":"#494843",
    "7":"#2c2c2a",
    "8":"#1b1a19",
    }


theme_dict["Transformer"]=transformer
theme_dict["Cold Lake"]=lake
theme_dict["Histgram"]=PEA_contrast
theme_dict["Curve"]=fill_between
theme_dict["Yellow"]=yellow
