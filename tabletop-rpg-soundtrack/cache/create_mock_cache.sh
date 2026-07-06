#!/bin/bash

set -e

declare -A THEMES

THEMES[dnd]="forest dungeon village"
THEMES[lovecraft]="asylum cemetery ritual"
THEMES[iron_kingdoms]="battlefield factory tavern"

for ost in "${!THEMES[@]}"; do

    mkdir -p "$ost"

    for theme in ${THEMES[$ost]}; do

        mkdir -p "$ost/$theme"/{songs,ambience,images}

        for i in 1 2 3; do
            touch "$ost/$theme/songs/file$i.mp3"
            touch "$ost/$theme/ambience/file$i.mp3"
            touch "$ost/$theme/images/file$i.png"
        done

    done

done

echo "Mock cache created successfully."


