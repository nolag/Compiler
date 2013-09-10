section .text

global __malloc
global __malloc_clear
__malloc:
__malloc_clear:
	;I think I can get rid of this
	push rdi
    push rax
    mov rax, 12  ; sys_brk system call
    mov rdi, 0   ; 0 bytes - query current brk
    syscall
    pop rdi
    push rax
    add rdi, rax ; move brk ahead by number of bytes requested
    mov eax, 12  ; sys_brk system call
    syscall
    pop rdi
    cmp rax, 0   ; on error, exit with code 22
    jne ok
    mov rax, 22
    ;I think I can get rid of this
    pop rdi
    call __debexit
ok:
    mov rax, rdi
   ;I think I can get rid of this
    pop rdi
    ret

; Debugging exit: ends the process, returning the value of rax as the exit code.
global __debexit
__debexit:	
	mov    rdi, rax
	mov    rax, 60   ; sys_exit system call
	syscall

; Exceptional exit: ends the process with exit code 13.
; Call this in cases where the Joos code would throw an exception.
    global __exception
__exception:
    mov rax, 60   ; sys_exit system call
    mov rdi, 13
    syscall

; Implementation of java.io.OutputStream.nativeWrite method.
; Outputs the low-order byte of rax to standard output.
global NATIVEjava.io.OutputStream.nativeWrite
NATIVEjava.io.OutputStream.nativeWrite:
	mov [char], al ; save the low order byte in memory
    mov rax, 1     ; sys_write system call
    mov rsi, char  ; address of bytes to write
    mov rdi, 1     ; stdout
    mov rdx, 1     ; number of bytes to write
    syscall
    mov rax, 0     ; return 0
    ret
    
char:
    dd 0