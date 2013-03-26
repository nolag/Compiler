package cs444.codegen;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.instructions.Add;
import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Global;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.Label;
import cs444.codegen.instructions.Mov;
import cs444.codegen.instructions.Movsx;
import cs444.codegen.instructions.Movzx;
import cs444.codegen.instructions.Neg;
import cs444.codegen.instructions.Pop;
import cs444.codegen.instructions.Push;
import cs444.codegen.instructions.Ret;
import cs444.codegen.instructions.Sar;
import cs444.codegen.instructions.factories.AddOpMaker;
import cs444.codegen.instructions.factories.AndOpMaker;
import cs444.codegen.instructions.factories.BinOpMaker;
import cs444.codegen.instructions.factories.BinUniOpMaker;
import cs444.codegen.instructions.factories.IDivMaker;
import cs444.codegen.instructions.factories.IMulMaker;
import cs444.codegen.instructions.factories.OrOpMaker;
import cs444.codegen.instructions.factories.SubOpMaker;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ProtectionLevel;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.ByteLiteralSymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.EmptyStatementSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.NullSymbol;
import cs444.parser.symbols.ast.ShortLiteralSymbol;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.parser.symbols.ast.SuperSymbol;
import cs444.parser.symbols.ast.ThisSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.parser.symbols.ast.expressions.AndExprSymbol;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;
import cs444.parser.symbols.ast.expressions.BinOpExpr;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;
import cs444.parser.symbols.ast.expressions.ForExprSymbol;
import cs444.parser.symbols.ast.expressions.IfExprSymbol;
import cs444.parser.symbols.ast.expressions.InstanceOfExprSymbol;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;
import cs444.parser.symbols.ast.expressions.NotOpExprSymbol;
import cs444.parser.symbols.ast.expressions.OrExprSymbol;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.ArrayPkgClassResolver;

public class CodeGenVisitor implements ICodeGenVisitor {
    private final List<Instruction> instructions = new LinkedList<Instruction>();
    private boolean hasEntry = false;
    private boolean getVal = false;

    @Override
    public void visit(MethodInvokeSymbol invoke) {
        //TODO
    }

    @Override
    public void visit(FieldAccessSymbol field) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(AInterfaceOrClassSymbol aInterfaceOrClassSymbol) {
        // TODO Auto-generated method stub
        for(ISymbol child : aInterfaceOrClassSymbol.children) child.accept(this);
    }

    @Override
    public void visit(MethodOrConstructorSymbol method) {
        instructions.add(new Comment("Start of method " + method.dclName));
        try{
            if(APkgClassResolver.generateUniqueName(method, method.dclName).equals(JoosNonTerminal.ENTRY)
                    && method.isStatic() && !hasEntry){

                hasEntry = true;
                instructions.add(new Global("_start"));
                instructions.add(new Label("_start"));
            }
        }catch(Exception e){
            //Should never get here.
            e.printStackTrace();
        }

        String methodName = null;

        try{
            methodName = method.dclInResolver.fullName.replace('.', '_') + "_" + ArrayPkgClassResolver.generateUniqueName(method, method.dclName);
        }catch(Exception e){ /*Should not get here ever */}

        if(method.getProtectionLevel() != ProtectionLevel.PRIVATE) instructions.add(new Global(methodName));
        instructions.add(new Label(methodName));

        for(ISymbol child : method.children) child.accept(this);

        instructions.add(new Comment("End of method " + method.dclName));
    }

    @Override
    public void visit(CreationExpression creationExpression) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ANonTerminal aNonTerminal) {
        for(ISymbol child : aNonTerminal.children) child.accept(this);
    }

    @Override
    public void visit(WhileExprSymbol whileExprSymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ForExprSymbol forExprSymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(IfExprSymbol ifExprSymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ReturnExprSymbol retSymbol) {
        if(retSymbol.children.size() == 1) retSymbol.children.get(0).accept(this);
        //value is already in eax from the rule therefore we just need to ret

        instructions.add(Ret.ret);
    }

    @Override
    public void visit(TypeSymbol typeSymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NameSymbol nameSymbol) {
        final DclSymbol dcl = nameSymbol.getLastLookupDcl();
        int offset = dcl.getOffset();
        if(getVal){
            int stackSize = dcl.getType().getTypeDclNode().stackSize;
            Size size = Size.DWORD;
            if(stackSize == 16) size = Size.WORD;
            if(stackSize == 8) size = Size.LOW;
            final InstructionArg from = new PointerRegister(Register.FRAME, offset);
            Instruction instruction;

            if(size == Size.DWORD){
                instruction = new Mov(Register.ACCUMULATOR, from);
            }else if(JoosNonTerminal.unsigned.contains(dcl.getType().getTypeDclNode().fullName)){
                instruction = new Movzx(Register.ACCUMULATOR, from, size);
            }else{
                instruction = new Movsx(Register.ACCUMULATOR, from, size);
            }

            instructions.add(new Comment("getting value of " + nameSymbol.getName()));
            instructions.add(instruction);
        }else{
            instructions.add(new Comment("getting location of " + nameSymbol.getName()));
            instructions.add(new Mov(Register.ACCUMULATOR, Register.FRAME));
            instructions.add(new Add(Register.ACCUMULATOR, new Immediate(String.valueOf(offset))));
        }
    }

    @Override
    public void visit(ATerminal terminal) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CastExpressionSymbol symbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NegOpExprSymbol op) {
        instructions.add(new Neg(Register.ACCUMULATOR));
    }

    @Override
    public void visit(NotOpExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(MultiplyExprSymbol op) {
        binUniOpHelper(op, IMulMaker.maker, false);
    }

    @Override
    public void visit(AssignmentExprSymbol op) {
        op.children.get(1).accept(this);
        instructions.add(new Push(Register.BASE));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.BASE));
        boolean tmp = getVal;
        getVal = true;
        op.children.get(0).accept(this);
        getVal = tmp;
        instructions.add(new Pop(Register.BASE));
    }

    @Override
    public void visit(DivideExprSymbol op) {
        binUniOpHelper(op, IDivMaker.maker, true);
    }

    @Override
    public void visit(RemainderExprSymbol op) {
        binUniOpHelper(op, IDivMaker.maker, true);
        instructions.add(new Mov(Register.ACCUMULATOR, Register.DATA));
    }

    @Override
    public void visit(AddExprSymbol op) {
        binOpHelper(op, AddOpMaker.maker);

    }

    @Override
    public void visit(SubtractExprSymbol op) {
        binOpHelper(op, SubOpMaker.maker);
    }

    @Override
    public void visit(LtExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(EqExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NeExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(AndExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(OrExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(EAndExprSymbol op) {
        binOpHelper(op, AndOpMaker.maker);
    }

    @Override
    public void visit(EOrExprSymbol op) {
        binOpHelper(op, OrOpMaker.maker);
    }

    @Override
    public void visit(LeExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(InstanceOfExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(IntegerLiteralSymbol intLiteral) {
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(String.valueOf(intLiteral.getValue()))));
    }

    @Override
    public void visit(NullSymbol nullSymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(BooleanLiteralSymbol boolSymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ThisSymbol thisSymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(SuperSymbol thisSymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(StringLiteralSymbol stringSymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CharacterLiteralSymbol characterSymbol) {
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(String.valueOf(characterSymbol.getValue()))));
    }

    @Override
    public void visit(EmptyStatementSymbol emptySymbol) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ArrayAccessExprSymbol arrayAccess) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(DclSymbol dclSymbol) {
        instructions.add(new Comment("dcl: " + dclSymbol.dclName));
        if(dclSymbol.children.isEmpty()) new IntegerLiteralSymbol(0).accept(this);
        else dclSymbol.children.get(0).accept(this);
        Size size = Size.DWORD;
        int stackSize = dclSymbol.getType().getTypeDclNode().stackSize;
        if(stackSize == 16) size = Size.WORD;
        if(stackSize == 8) size = Size.LOW;
        instructions.add(new Push(Register.ACCUMULATOR, size));
        instructions.add(new Comment("end dcl: " + dclSymbol.dclName));
    }

    private void binOpHelper(BinOpExpr bin, BinOpMaker maker){
        instructions.add(new Push(Register.BASE));
        bin.children.get(1).accept(this);
        instructions.add(new Mov(Register.ACCUMULATOR, Register.BASE));
        bin.children.get(0).accept(this);
        instructions.add(maker.make(Register.ACCUMULATOR, Register.BASE));
        instructions.add(new Pop(Register.BASE));
    }

    private void binUniOpHelper(BinOpExpr bin, BinUniOpMaker maker, boolean sar){
        instructions.add(new Push(Register.BASE));
        bin.children.get(1).accept(this);
        instructions.add(new Mov(Register.ACCUMULATOR, Register.BASE));
        bin.children.get(0).accept(this);

        if(sar){
            instructions.add(new Mov(Register.DATA, Register.ACCUMULATOR));
            instructions.add(Sar.acc32);
        }

        instructions.add(maker.make(Register.BASE));
        instructions.add(new Pop(Register.BASE));
    }

    @Override
    public void visit(ByteLiteralSymbol byteLiteral) {
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(String.valueOf(byteLiteral.getValue()))));
    }

    @Override
    public void visit(ShortLiteralSymbol shortLiteral) {
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(String.valueOf(shortLiteral.getValue()))));
    }

    @Override
    public void printToFileAndEmpty(PrintStream printer){
        for(Instruction instruction : instructions) printer.println(instruction.generate());
        instructions.clear();
    }
}
