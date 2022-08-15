#pragma once

#include <cassert>
#include "IThread.h"

#define MUTEX_ASSERT(o) assert(o)
#ifdef WIN32
	//#include "winnt.h"
    #ifndef INVALID_HANDLE
    #define INVALID_HANDLE ((HANDLE) -1)
    #define XPLAT_MUTEX_TYPE HANDLE
    #endif  // INVALID_HANDLE
    #define ASSERT_MUTEX_CREATE(m)          MUTEX_ASSERT((m = CreateMutex(NULL, FALSE, NULL)) != INVALID_HANDLE)
    #define ASSERT_MUTEX_DESTROY(m)         MUTEX_ASSERT((CloseHandle(m) != 0))
                                            // XXX: vvv absolutely hates ABANDONNED
    #define ASSERT_MUTEX_LOCK(m)            MUTEX_ASSERT((WaitForSingleObject((m),INFINITE) == WAIT_OBJECT_0))
    #define ASSERT_MUTEX_UNLOCK(m)          MUTEX_ASSERT((ReleaseMutex((m)) != 0))
#else   // WIN32
    #include <pthread.h>
    #define XPLAT_MUTEX_TYPE pthread_mutex_t
    #define ASSERT_MUTEX_CREATE(m)          pthread_mutex_init(&(m),NULL)               // cannot fail
    #define ASSERT_MUTEX_DESTROY(m)         MUTEX_ASSERT((pthread_mutex_destroy(&(m)) == 0))
    #define ASSERT_MUTEX_LOCK(m)            MUTEX_ASSERT((pthread_mutex_lock(&(m)) == 0))
    #define ASSERT_MUTEX_UNLOCK(m)          MUTEX_ASSERT((pthread_mutex_unlock(&(m)) == 0))
#endif  // WIN32

class BaseMutex : public ILockable
{
public:
	                    BaseMutex()
                        { ASSERT_MUTEX_CREATE(m); }
	virtual             ~BaseMutex()
                        { ASSERT_MUTEX_DESTROY(m); }
	virtual bool        Acquire()
                        { ASSERT_MUTEX_LOCK(m); return true; }
	virtual bool        Release()
                        { ASSERT_MUTEX_UNLOCK(m); return true; }
protected:

    XPLAT_MUTEX_TYPE    m;
};
