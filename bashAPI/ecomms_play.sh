#!/bin/bash
SONG_NAME=${2}
SONG_ID="";
CACHE_FILE='/ecomms/cache/musics/cache.lst'

function get_id() {

    ID="$(yt-dlp --no-warnings "ytsearch1: ${SONG_NAME}" --get-id)";
    SONG_ID=$ID;
}

if [ -f ${CACHE_FILE} ]; then

    CACHED_ID="$(grep "${SONG_NAME}" /ecomms/cache/musics/cache.lst | cut -d ':' -f1)";
    if [ "${CACHED_ID}" != "" ]; then SONG_ID=$CACHED_ID; else get_id; fi
else get_id; fi

function download() {

    COMMAND="$(yt-dlp -f bestaudio --extract-audio --audio-format mp3 "https://www.youtube.com/watch?v=${SONG_ID}" -o "/ecomms/cache/musics/${SONG_ID}.mp3" --no-warnings -q)";
    echo "${SONG_ID}:${SONG_NAME}" >> /ecomms/cache/musics/cache.lst
}

function play() {

    FILE=/ecomms/cache/musics/${SONG_ID}.mp3;
    if [ ! -f ${FILE} ]; then download; fi

    echo "$FILE";
}


if [ "${1}" == "--id" ]; then get_id;
elif [ "${1}" == "--play" ]; then play; fi
