default rel    ; RIP-relative adresses

section .text

extern  GetStdHandle
extern  WriteFile
extern  ExitProcess
extern GetProcessHeap
extern HeapAlloc

; Allocates eax bytes of memory. Pointer to allocated memory returned in eax.
global __malloc
global __malloc_clear
__malloc:
	mov rsi, rax ;back up amount of space
	;make shadow space
	sub rsp, 32
	;current process heap is an arg
	call GetProcessHeap ;get the current process heap
	mov rcx, rax
	;end of GetProcessHeap arg
	mov r8, rsi ; number of bytes
	mov rdx, 0; no flags
	call HeapAlloc
	cmp rax, 0
	je __mallocfail
	;remove shadow space
	add rsp, 32
	ret
	
	
__malloc_clear:
	mov rsi, rax ;back up amount of space
	;make shadow space
	sub rsp, 32
	;current process heap is an arg
	call GetProcessHeap ;get the current process heap
	mov rcx, rax
	;end of GetProcessHeap arg
	mov r8, rsi ; number of bytes
	mov rdx, 8; HEAPZEROMEMORY
	call HeapAlloc
	cmp rax, 0
	je __mallocfail
	;remove shadow space
	add rsp, 32
	ret
	
__mallocfail:
	;make shadow space
	sub rsp, 32
	push 22
	call ExitProcess

; Debugging exit: ends the process, returning the value of eax as the exit code.
global __debexit
__debexit:
	;make shadow space
	sub rsp, 32
    mov rcx, rax
    call ExitProcess

; Exceptional exit: ends the process with exit code 13.
; Call this in cases where the Joos code would throw an exception.
    global __exception
__exception:
	;make shadow space
	sub rsp, 32
    mov rcx, 13
    call ExitProcess

;TODO

; Implementation of java.io.OutputStream.nativeWrite method.
; Outputs the low-order byte of eax to standard output.
global NATIVEjava.io.OutputStream.nativeWrite
NATIVEjava.io.OutputStream.nativeWrite:
	mov [char], al
	;make shadow space + space for last var
	sub rsp, 40
	;GetStdHandle( STDOUTPUTHANDLE)
    mov rcx, -11
    call GetStdHandle
	mov rcx, rax
	;end GetStdHandle( STDOUTPUTHANDLE)
	; WriteFile( stdout, char, 1, 0, 0);
    xor r9, r9
	mov [rsp + 32], r9
    mov r8, 1
    mov rdx, char
    call    WriteFile
	;remove shadow space + space for last var
	add rsp, 40
	ret

section .data

char:
    db 0
