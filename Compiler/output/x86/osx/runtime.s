section .text

global _start
_start:
extern entry
call entry
push eax
mov eax, 1 ;1 is exit
sub esp, 4 ;osx / bsd leave 4 bytes
int 0x80 ;software interupt

; Allocates eax bytes of memory. Pointer to allocated memory returned in eax.
global __malloc
global __malloc_clear
__malloc:
__malloc_clear:
    push 0 ;no offset to start reading decriptor
    push -1 ;no file decriptor
    push 0x1000 ;MAP_ANON, no file
    push 3 ;PROT_READ|PROT_WRITE
    push eax
    push 0 ;we don't have our own memory to map to
    sub esp, 4 ;osx/bsd leave 4 bytes
    mov eax, 197
    int 0x80
    add esp, 28 ;restore stack
    cmp eax, -1 ;MAP_FAILED
    jne ok
    mov eax, 22
    call __debexit
ok:
    ret

; Debugging exit: ends the process, returning the value of eax as the exit code.
global __debexit
__debexit:
    push eax
    mov eax, 1   ; sys_exit system call
    sub esp, 4 ;osx / bsd leave 4 bytes
    int 0x80

; Exceptional exit: ends the process with exit code 13.
; Call this in cases where the Joos code would throw an exception.
    global __exception
__exception:
    mov eax, 1   ; sys_exit system call
    push 13
    sub esp, 4 ;osx / bsd leave 4 bytes
    int 0x80

; Implementation of java.io.OutputStream.nativeWrite method.
; Outputs the low-order byte of eax to standard output.
global NATIVEjava.io.OutputStream.nativeWrite
NATIVEjava.io.OutputStream.nativeWrite:
    mov [char], al ; save the low order byte in memory
    mov eax, 4     ; sys_write system call
    push 1     ; number of bytes to write
    push char  ; address of bytes to write
    push 1     ; stdout
    sub esp, 4 ;osx / bsd leave 4 bytes
    int 0x80
    add esp, 16 ;clean up
    mov eax, 0     ; return 0
    ret

section .data

char:
    dd 0
