cmake_minimum_required(VERSION 3.4.1)

add_library(alox SHARED IMPORTED)
#add_library(ipcamera SHARED IMPORTED)
add_library(tennisbase SHARED IMPORTED)
add_library(tennisbout SHARED IMPORTED)
#add_library(tennisdrill SHARED IMPORTED)

set_target_properties(
                    alox
                    PROPERTIES IMPORTED_LOCATION
                    libs/${ANDROID_ABI}/libalox_1702R1.so
                    )

#set_target_properties(
#                    ipcamera
#                    PROPERTIES IMPORTED_LOCATION
#                    libs/${ANDROID_ABI}/libipcamera.so
#                    )

set_target_properties(
                    tennisbase
                    PROPERTIES IMPORTED_LOCATION
                    libs/${ANDROID_ABI}/libtennisbase.so
                    )

set_target_properties(
                    tennisbout
                    PROPERTIES IMPORTED_LOCATION
                    libs/${ANDROID_ABI}/libtennisbout.so
                    )

#set_target_properties(
#                    tennisdrill
#                    PROPERTIES IMPORTED_LOCATION
#                    libs/${ANDROID_ABI}/libtennisdrill.so
#                    )