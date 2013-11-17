section .text

extern  _GetStdHandle@4
extern  _WriteFile@20
extern  _ExitProcess@4
extern _GetProcessHeap@0
extern _HeapAlloc@12

global _main
_main:
extern entry
call entry
push eax
call _ExitProcess@4

; Allocates eax bytes of memory. Pointer to allocated memory returned in eax.
global __malloc
global __malloc_clear
__malloc:
	push eax ;number of bytes to allocate
	push 0; no flags
	;current process heap is an arg
	call _GetProcessHeap@0 ;get the current process heap
	push eax;
	;end of _GetProcessHeap arg
	call _HeapAlloc@12
	cmp eax, 0
	je __malloc_fail
	ret
	
	
__malloc_clear:
	push eax ;number of bytes to allocate
	push 8; HEAP_ZERO_MEMORY
	;current process heap is an arg
	call _GetProcessHeap@0 ;get the current process heap
	push eax;
	;end of _GetProcessHeap arg
	call _HeapAlloc@12
	cmp eax, 0
	je __malloc_fail
	ret
	
__malloc_fail:
	push 22
	call _ExitProcess@4

; Debugging exit: ends the process, returning the value of eax as the exit code.
global __debexit
__debexit:
    push eax
    call _ExitProcess@4

; Exceptional exit: ends the process with exit code 13.
; Call this in cases where the Joos code would throw an exception.
    global __exception
__exception:
    push 13
    call _ExitProcess@4

; Implementation of java.io.OutputStream.nativeWrite method.
; Outputs the low-order byte of eax to standard output.
global NATIVEjava.io.OutputStream.nativeWrite
NATIVEjava.io.OutputStream.nativeWrite:
	mov [char], al
    ; WriteFile( stdout, char, 1, 0, 0);
    push    0
    push 	0
    push    1
    push    char
	;GetstdHandle( STD_OUTPUT_HANDLE)
    push    -11
    call    _GetStdHandle@4
	;end GetstdHandle( STD_OUTPUT_HANDLE)
	push eax ; push result
    call    _WriteFile@20
	ret

section .data

char:
    db 0
