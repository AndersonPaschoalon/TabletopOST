#!/usr/bin/env python3

import json
from pathlib import Path

AUDIO_EXTENSIONS = {".mp3", ".wav"}
IMAGE_EXTENSIONS = {".png", ".jpg", ".jpeg", ".gif", ".webp"}


def list_files(directory: Path, extensions):
    """
    Lista todos os arquivos válidos de um diretório,
    retornando caminhos relativos ao diretório da OST.
    """
    return sorted(
        [
            str(file.relative_to(directory.parent.parent)).replace("\\", "/")
            for file in directory.iterdir()
            if file.is_file() and file.suffix.lower() in extensions
        ]
    )


def build_theme(theme_dir: Path):
    """
    Constrói um Theme a partir de um diretório.

    Retorna None caso o diretório não represente um Theme válido.
    """

    songs_dir = theme_dir / "songs"
    ambience_dir = theme_dir / "ambience"
    images_dir = theme_dir / "images"

    if not songs_dir.is_dir():
        return None

    if not ambience_dir.is_dir():
        return None

    if not images_dir.is_dir():
        return None

    songs = list_files(songs_dir, AUDIO_EXTENSIONS)
    ambience = list_files(ambience_dir, AUDIO_EXTENSIONS)
    images = list_files(images_dir, IMAGE_EXTENSIONS)

    if not songs:
        return None

    if not ambience:
        return None

    if not images:
        return None

    return {
        "name": theme_dir.name,
        "songs": songs,
        "ambience": ambience,
        "images": images,
    }


def generate_manifest(ost_dir: Path):

    themes = []

    for entry in sorted(ost_dir.iterdir()):

        if not entry.is_dir():
            continue

        theme = build_theme(entry)

        if theme is None:
            print(f"  Ignoring invalid theme: {entry.name}")
            continue

        print(f"  Adding theme: {entry.name}")

        themes.append(theme)

    if not themes:
        print(f"No valid themes found for OST '{ost_dir.name}'.")
        return

    manifest = {
        "name": ost_dir.name,
        "themes": themes,
    }

    manifest_path = ost_dir / "manifest.json"

    with open(manifest_path, "w", encoding="utf-8") as fp:
        json.dump(manifest, fp, indent=4)

    print(f"Generated {manifest_path}")


def main():

    root = Path.cwd()

    print(f"Scanning cache: {root}")

    for entry in sorted(root.iterdir()):

        if not entry.is_dir():
            continue

        print(f"\nProcessing OST: {entry.name}")

        generate_manifest(entry)


if __name__ == "__main__":
    main()