/******************************<%BEGIN LICENSE%>******************************/
// (c) Copyright 2013 Lumidigm, Inc. (Unpublished Copyright) ALL RIGHTS RESERVED.
//
// For a list of applicable patents and patents pending, visit www.lumidigm.com/patents/
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
// FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
// IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
//
/******************************<%END LICENSE%>******************************/
#pragma once

// INTEGER TYPE AND RANGE DEFINITIONS FOR VARIOUS OSs/COMPILERs

#ifdef _WIN32

// 32-bit Windows
#define LUMI_WORDSIZE (32)
#define LUMI_I64_FMT "%lld"
#define LUMI_U64_FMT "%llu"

#if (_MSC_VER >= 1600) //VS2010 and up include stdint.h
#include <stdint.h>
#else
typedef char             int8_t;
typedef short            int16_t;
typedef int              int32_t;
typedef __int64          int64_t;

typedef unsigned char    uint8_t;
typedef unsigned short   uint16_t;
typedef unsigned int     uint32_t;
typedef unsigned __int64 uint64_t;

#define INT8_MAX   ((int8_t)  0x7F)
#define INT16_MAX  ((int16_t) 0x7FFF)
#define INT32_MAX  ((int32_t) 0x7FFFFFFF)
#define INT64_MAX  ((int64_t) INT64_CONSTANT(0x7FFFFFFFFFFFFFFF))

#define INT8_MIN   ((int8_t)  0x80)
#define INT16_MIN  ((int16_t) 0x8000)
#define INT32_MIN  ((int32_t) 0x80000000)
#define INT64_MIN  ((int64_t) (INT64_CONSTANT(0x1) << 63))

#define UINT8_MAX  ((uint8_t)  0xFF)
#define UINT16_MAX ((uint16_t) 0xFFFF)
#define UINT32_MAX ((uint32_t) 0xFFFFFFFF)
#define UINT64_MAX ((uint64_t) (UINT64_CONSTANT(0xFFFFFFFFFFFFFFFF)))
#endif

//#if LUMI_WORDSIZE == 64
//#define INT64_CONSTANT(c)  (c ## L)
//#define UINT64_CONSTANT(c) (c ## LU)
//#endif

#if LUMI_WORDSIZE == 32
#define INT64_CONSTANT(c)  (c)
#define UINT64_CONSTANT(c) (c ## LLU)
#endif

#else   // WIN32
#ifdef __GNUC__

// GNU C, 32 or 64 bit
#include <stdint.h>
typedef int64_t   __int64;

#if defined(__i386__)

// 32-bit GNU C
#define LUMI_WORDSIZE   (32)
#define LUMI_I64_FMT "%lld"
#define LUMI_U64_FMT "%llu"

#elif defined(__x86_64__)

// 64-bit GNU C
#define LUMI_WORDSIZE (64)
#define LUMI_I64_FMT "%ld"
#define LUMI_U64_FMT "%lu"

#elif defined(__arm__)

// 32-bit GNU C
#define LUMI_WORDSIZE (32)
#define LUMI_I64_FMT "%lld"
#define LUMI_U64_FMT "%llu"

#else

#error "Unknown GNU platform"

#endif
#else   // __GNUC__

#ifdef _VDSP

#include <stdint.h>

#define LUMI_WORDSIZE (32)
#define LUMI_I64_FMT "%lld"
#define LUMI_U64_FMT "%llu"

#endif  // _VDSP

#endif  // else __GNUC__

//typedef int64_t  __int64;
//typedef uint64_t __uint64;

#endif  // else WIN32
