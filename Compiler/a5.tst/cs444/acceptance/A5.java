package cs444.acceptance;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class A5 {

    @Test
    public void testCompileProgramsNoStdLib() throws IOException, InterruptedException{
        TestHelper.assertReturnCodeForFiles("JoosPrograms/A5/NoStdLibPrograms/", 0, true, false,
                true, Collections.<String>emptyList(), new AsmAndLinkCallback());
    }

    @Test
    public void testMarmosetPrograms() throws IOException, InterruptedException{
        List<String> ignoreList = new LinkedList<String>(Arrays.asList(
                "J1_random_arithmetic.java",
                "J1_random_arithmetic_var.java",
                "J1_1_Instanceof_InLazyExp.java",
                "J1_1_Instanceof_OfAdditiveExpression.java",
                "J1_1_Instanceof_OfCastExpression.java",
                "J1_300locals.java",
                "J1_6_Assignable_Object_ObjectArray.java",
                "J1_6_AssignmentInArrayLength.java",
                "J1_A_AddressNotEqual.java",
                "J1_A_ArrayBaseInAssignment.java",
                "J1_A_ArrayStoreLoad.java",
                "J1_A_AssignmentInLazyOr.java",
                "J1_A_BooleanArray_External.java",
                "J1_A_CloneOnInterface",
                "J1_A_CloneWithArgs.java",
                "J1_A_Complement_SideEffect.java",
                "J1_A_ConcatInSimpleInvoke.java",
                "J1_A_ConcatInStaticInvoke.java",
                "J1_A_Conditionals_NoInstructionAfterIfElse.java",
                "J1_A_FieldInitialization_Before.java",
                "J1_A_FieldInitialization_NonConstant_Before.java",
                "J1_A_GreaterOrEqual.java",
                "J1_A_LazyEagerAndOr.java",
                "J1_A_LazyEval.java",
                "J1_arrayAccess.java",
                "J1_ArrayCreateAndIndex.java",
                "J1_arrayinstanceof1.java",
                "J1_arrayinstanceof2.java",
                "J1_array.java",
                "J1_A_String_ByteShortCharInt.java",
                "J1_A_StringConstAEQ_ANE.java",
                "J1_backwardRef.java",
                "J1_BigByteInit.java",
                "J1_BigCharCharInit.java",
                "J1_BigShortFromByteInit.java",
                "J1_BigShortInit.java",
                "J1_callbeforereturn",
                "J1_charadd.java",
                "J1_concat_in_binop.java",
                "J1_concatInMethods.java",
                "J1_constructorbodycast.java",
                "J1_divdiv.java",
                "J1e_A_CastNewExp",
                "J1e_A_CastToArray.java",
                "J1e_A_CastToString.java",
                "J1e_divisionbyzero.java",
                "J1_fieldinit_forward_ref2.java",
                "J1_fieldinit_forward_ref.java",
                "J1_fieldinit.java",
                "J1_Hello.java",
                "J1_implicitstringconcatenation.java",
                "J1_instanceof_array2.java",
                "J1_instanceof_array.java",
                "J1_intstringadd.java",
                "J1_minuschar.java",
                "J1_minusminusminus.java",
                "J1_NamedTypeArray.java",
                "J1_NegativeByteCast.java",
                "J1_NegativeCharCast.java",
                "J1_NegativeIntCast1.java",
                "J1_NegativeIntCast2.java",
                "J1_negativeintcast3.java",
                "J1_NegativeOneByteByteCast.java",
                "J1_NegativeOneByteCharCast.java",
                "J1_NegativeOneByteIntCast.java",
                "J1_NegativeOneByteShortCast.java",
                "J1_NegativeShortCast.java",
                "J1_nestedcast.java",
                "J1_sideeffect1.java",
                "J1_sideeffect2.java",
                "J1_sideeffect3.java",
                "J1_sideeffect4.java",
                "J1_sideeffect5.java",
                "J1_sideeffect6.java",
                "J1_sideeffect8.java",
                "J1_sideeffects_array2.java",
                "J1_sideeffects_array3.java",
                "J1_sideeffects_array4.java",
                "J1_sideeffects_array.java",
                "J1_sideeffects_obj",
                "J1_sideeffects_obj2.java",
                "J1_sideeffects_obj3.java",
                "J1_sim_and.java",
                "J1_sim_or.java",
                "J1_SimpleTypeArray.java",
                "J1_sim_xor.java",
                "J1_SmallInt.java",
                "J1_StaticField_AccessFromClass.java",
                "J1_staticMethodInvocation.java",
                "J1_stringadd.java",
                "J1_StringCast.java",
                "J1_stringconcat.java",
                "J1_toomuchinc.java",
                "J1_typecheck_array.java",
                "J1_typecheck_expstm.java",
                "J1_typecheck_plus.java",
                "J1_while1.java",
                "J1_while2.java",
                "J1_whiletrue1.java",
                "J1_WildConcat.java",
                "J2_A_FieldInitialization_Static_Before.java",
                "J2_A_FieldInitialization_Static_NonConstant_Before.java",
                "J2_fieldinit_forward_ref.java",
                "J2_forwardRef.java"));

        Map<String, String> env = System.getenv();

        if (env.containsKey("all_tests")){
            TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a5/", 0, true, true,
                    true, Collections.<String>emptyList(), new AsmAndLinkCallback());
        }else{
            TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a5/", 0, true, true,
                    true, ignoreList, new AsmAndLinkCallback());
        }
    }
}

