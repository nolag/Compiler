;OSX 64 bit system calls add 0x20000000 to the call number
section .text

global _start
_start:
extern entry
call entry
mov rdi, rax
mov rax, 0x2000001 ;1 is exit
sub rsp, 8 ;osx / bsd leave 8 bytes
syscall ;software interupt

; Allocates eax bytes of memory. Pointer to allocated memory returned in eax.
global __malloc
global __malloc_clear
__malloc:
__malloc_clear:
    mov r8, 0 ;no offset to start reading decriptor
    mov r9, -1 ;no file decriptor
    mov r10, 0x1000 ;MAP_ANON, no file
    mov rdx, 3 ;PROT_READ|PROT_WRITE
    mov rsi, rax
    mov rdi, 0 ;we don't have our own memory to map to:
    mov rax, 0x20000C5 ;197 = 0xC5 is mmap
    sub rsp, 8 ;osx/bsd leave 8 bytes
    syscall
    add rsp, 8 ;clean up
    cmp rax, -1 ;MAP_FAILED
    jne ok
    mov rax, 22
    call __debexit
ok:
    ret

; Debugging exit: ends the process, returning the value of eax as the exit code.
global __debexit
__debexit:
    mov rdi, rax
    mov rax, 0x2000001 ;1 is exit
    sub rsp, 8 ;osx / bsd leave 8 bytes
    syscall

; Exceptional exit: ends the process with exit code 13.
; Call this in cases where the Joos code would throw an exception.
    global __exception
__exception:
    mov rdi, 13
    mov rax, 0x2000001 ;1 is exit
    sub rsp, 8 ;osx / bsd leave 8 bytes
    syscall

; Implementation of java.io.OutputStream.nativeWrite method.
; Outputs the low-order byte of eax to standard output.
global NATIVEjava.io.OutputStream.nativeWrite
NATIVEjava.io.OutputStream.nativeWrite:
    mov rdx, char
    mov [rdx], al ; save the low order byte in memory
    mov rax, 0x2000004     ; sys_write system call
    mov rdx, 1     ; number of bytes to write
    mov rsi, char  ; address of bytes to write
    mov rdi, 1     ; stdout
    sub rsp, 8 ;osx / bsd leave 8 bytes
    syscall
    add rsp, 8 ;clean up
    mov rax, 0     ; return 0
    ret

section .data

char:
    dd 0
