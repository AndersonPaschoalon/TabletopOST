#!/bin/bash
#
# run.sh —> roda o TabletopOST via Docker (Linux apenas por questões de drivers de audio)
#
# Uso:
#   ./run.sh          -> abre a interface gráfica (GUI)
#   ./run.sh --cli    -> abre o modo terminal (CLI)
#
# Pré-requisitos (rodar uma vez):
#   xhost +local:docker
#
# A imagem precisa já ter sido buildada:
#   docker build -t tabletopost:latest .

set -euo pipefail

IMAGE="tabletopost:latest"

mkdir -p local_storage cache


PULSE_COOKIE_HOST=""
if [ -f "${HOME}/.config/pulse/cookie" ]; then
    PULSE_COOKIE_HOST="${HOME}/.config/pulse/cookie"
elif [ -f "${HOME}/.pulse-cookie" ]; then
    PULSE_COOKIE_HOST="${HOME}/.pulse-cookie"
fi

PULSE_ARGS=()
if [ -n "$PULSE_COOKIE_HOST" ]; then
    PULSE_ARGS+=(-e "PULSE_COOKIE=/tmp/pulse-cookie" -v "${PULSE_COOKIE_HOST}:/tmp/pulse-cookie:ro")
fi

docker run --rm -it \
  --name tabletopost \
  -e DISPLAY="${DISPLAY:-:0}" \
  -e SDL_AUDIODRIVER=pulse \
  -e PULSE_SERVER=unix:/tmp/pulse-socket \
  "${PULSE_ARGS[@]}" \
  -v /tmp/.X11-unix:/tmp/.X11-unix:rw \
  -v "${XDG_RUNTIME_DIR:-/run/user/$(id -u)}/pulse/native:/tmp/pulse-socket" \
  -v "$(pwd)/local_storage:/app/local_storage" \
  -v "$(pwd)/cache:/app/cache" \
  --user "$(id -u):$(id -g)" \
  --ipc host \
  --network host \
  "$IMAGE" "$@"
