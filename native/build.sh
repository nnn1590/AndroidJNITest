#!/usr/bin/env bash
set -e

declare BASE_DIR
BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]:-${0}}")"; pwd)"
readonly BASE_DIR
cd "${BASE_DIR}"

declare -r NDK_ROOT_R6="${NDK_ROOT_R6:-${HOME}/Android/android-ndk-r6}"
declare -r NDK_ROOT_R18B="${NDK_ROOT_R18B:-${HOME}/Android/android-ndk-r18b}"
declare -r OUT="out"
declare -r OUT_V1_ARMEABI="out_v1_armeabi"
declare -r OUT_V1_ARMEABIV7A="out_v1_armeabi-v7a"
declare -r OUT_V1_X86="out_v1_x86"
declare -r OUT_V21="out_v21"

rm -rf "${OUT_V1_ARMEABI}" "${OUT_V1_ARMEABIV7A}" "${OUT_V1_X86}" "${OUT_V21}" "${OUT}"
mkdir "${OUT_V1_ARMEABI}" "${OUT_V1_ARMEABIV7A}" "${OUT_V1_X86}" "${OUT_V21}" "${OUT}"

cp -a jni_base "${OUT_V1_ARMEABI}/jni"
cp -a jni_base "${OUT_V1_ARMEABIV7A}/jni"
cp -a jni_base "${OUT_V1_X86}/jni"
mv "${OUT_V1_ARMEABI}/jni/"{_AndroidV1,Android}.mk
mv "${OUT_V1_ARMEABIV7A}/jni/"{_AndroidV1,Android}.mk
mv "${OUT_V1_X86}/jni/"{_AndroidV1,Android}.mk
mv "${OUT_V1_ARMEABI}/jni/"{_ApplicationV1_armeabi,Application}.mk
mv "${OUT_V1_ARMEABIV7A}/jni/"{_ApplicationV1_armeabi-v7a,Application}.mk
mv "${OUT_V1_X86}/jni/"{_ApplicationV1_x86,Application}.mk
cp -a jni_base "${OUT_V21}/jni"
mv "${OUT_V21}/jni/"{_AndroidV21,Android}.mk
mv "${OUT_V21}/jni/"{_ApplicationV21,Application}.mk

for i in "${OUT_V1_ARMEABI}" "${OUT_V1_ARMEABIV7A}" "${OUT_V1_X86}"; do
	pushd "${i}"
	"${NDK_ROOT_R6}/ndk-build"
	popd
done

pushd "${OUT_V21}"
"${NDK_ROOT_R18B}/ndk-build"
popd

mkdir "${OUT}/libs"
for i in "${OUT_V1_ARMEABI}" "${OUT_V1_ARMEABIV7A}" "${OUT_V1_X86}" "${OUT_V21}"; do
	cp -rT "${i}/libs" "${OUT}/libs"
done
