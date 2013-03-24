package cs444.codegen;

import java.util.LinkedList;
import java.util.List;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.Mov;
import cs444.codegen.instructions.Movsx;
import cs444.codegen.instructions.Movzx;
import cs444.codegen.instructions.Neg;
import cs444.codegen.instructions.Pop;
import cs444.codegen.instructions.Push;
import cs444.codegen.instructions.Ret;
import cs444.codegen.instructions.factories.AddOpMaker;
import cs444.codegen.instructions.factories.AndOpMaker;
import cs444.codegen.instructions.factories.BinOpMaker;
import cs444.codegen.instructions.factories.OrOpMaker;
import cs444.codegen.instructions.factories.SubOpMaker;
import cs444.parser.symbols.ATerminal;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.ByteLiteralSymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.EmptyStatementSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.ListedSymbol;
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

public class CodeGenVisitor implements ISymbolChoiceVisitor {
    private final List<Instruction> instructions = new LinkedList<Instruction>();

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

    }

    @Override
    public void visit(MethodOrConstructorSymbol method) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CreationExpression creationExpression) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NonTerminal aNonTerminal) {
        // TODO Auto-generated method stub

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
        //TODO anything with more than one lookup link
        final DclSymbol dcl = nameSymbol.getLastLookupDcl();
        int offset = dcl.getOffset();
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

        instructions.add(instruction);
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
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(AssignmentExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(DivideExprSymbol op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(RemainderExprSymbol op) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ListedSymbol listedSymbol) {
        // TODO Auto-generated method stub

    }

    private void binOpHelper(BinOpExpr bin, BinOpMaker maker){
        instructions.add(new Push(Register.BASE));
        bin.children.get(0).accept(this);
        instructions.add(new Mov(Register.ACCUMULATOR, Register.BASE));
        bin.children.get(1).accept(this);
        instructions.add(maker.make(Register.ACCUMULATOR, Register.BASE));
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
}
