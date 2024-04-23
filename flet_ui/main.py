"""
======================================================================
MAIN ---
Main file for the coloset UI.

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

from typing import Dict

from data import theme_dict

import flet as ft

def main(page: ft.Page):
    ## setting Pages
    page.title = "ThemeHub: Custmizing Your Themes in Figures"
    page.vertical_alignment = ft.MainAxisAlignment.CENTER
    page.bgcolor=ft.colors.GREY_900
    page.horizontal_alignment=ft.CrossAxisAlignment.CENTER

    # if page.platform == ft.PagePlatform.MACOS:
    #     page.add(ft.CupertinoDialogAction("Cupertino Button"))
    # else:
    #     page.add(ft.TextButton("Material Button"))


    ## ----- TOP PENALS
    title="ThemeHub: Gallery of Themes in Figures"
    title_c=ft.Text(
            spans=[
                ft.TextSpan(
                    title,
                    ft.TextStyle(
                        size=40,
                        weight=ft.FontWeight.BOLD,
                        foreground=ft.Paint(
                            gradient=ft.PaintLinearGradient(
                                (3, 100), (5, 20), [ft.colors.BLUE_800, ft.colors.PURPLE_800]
                            )
                        ),
                    ),
                ),
            ],
        text_align=ft.TextAlign.CENTER
        )
    page.add(title_c)
    themes=showThemes(theme_dict,page)

    page.add(themes)


    ## ----- BOTTOM PENALS
    page.add(ft.Text("Moreover AI 2024 @All Rights Reserved",
                    size=15,
                    color=ft.colors.BLUE_600,
                    weight=ft.FontWeight.NORMAL,
                     ))





def showThemes(theme_dict:Dict[str, Dict[str,str]],page:ft.Page):
    theme_ls=[]
    for theme_name in theme_dict:
        theme=theme_dict[theme_name]
        atheme=showATheme(theme_name,theme,page)
        theme_ls.append(atheme)

    themes=ft.Row(controls=theme_ls,
                  alignment=ft.MainAxisAlignment.CENTER,
                  vertical_alignment=ft.CrossAxisAlignment.START,
                  )
    return themes

def showATheme(theme_name:str, theme_dict:Dict[str,str],page:ft.Page):
    t_title=ft.Text(
            spans=[
                ft.TextSpan(
                    theme_name,
                    ft.TextStyle(
                        size=30,
                        weight=ft.FontWeight.BOLD,
                        foreground=ft.Paint(
                            gradient=ft.PaintLinearGradient(
                                (3, 150),
                                (6, 10),
                                [ft.colors.BLUE_800,
                                 ft.colors.PURPLE_800]
                            )
                        ),
                    ),
                ),
            ],
        text_align=ft.TextAlign.CENTER
        )

    color_ls=[]
    for color,hexx in theme_dict.items():
        temp=\
            ft.Card(
                width="180",
                height="100",
                content=ft.TextButton(
                    "",
                    data=hexx,
                    on_click=lambda e:\
                    page.set_clipboard(e.control.data),
                    ),
                    color=hexx)
        color_ls.append(
            temp
            )

    theme_ls=ft.Column(controls=color_ls,
                        )
    atheme=ft.Column(
        alignment=ft.MainAxisAlignment.CENTER,
        horizontal_alignment=ft.CrossAxisAlignment.CENTER,
            controls=[t_title, theme_ls]
            )
    return atheme


app=ft.app(main,export_asgi_app=True)



