/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package end2ast

import io.openvalidation.common.ast.ASTComparisonOperator
import io.openvalidation.common.data.DataPropertyType
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ArrayTest {

    @Test
    fun array_one_of_explicit_three_members(){
        var input = """
            IF name ONE OF Boris, Donald, Jimmy
            THEN unfall
        """

        End2AstRunner.run(input, "{name:\"String\"}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .condition()
                        .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
                        .leftProperty()
                            .hasPath("name")
                    .parentCondition()
                        .rightArray()
                            .hasSize(3)
                            .StringAtPosition(0)
                                .hasValue("Boris")
                        .parentArray()
                            .StringAtPosition(1)
                                .hasValue("Donald")
                        .parentArray()
                            .StringAtPosition(2)
                                .hasValue("Jimmy")
        }
    }

    @Test
    fun array_one_of_implicit_three_members(){
        var input = """
            IF name EQUALS Boris, Donald, Jimmy
            THEN unfall
        """

        End2AstRunner.run(input, "{name:\"String\"}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .condition()
                        .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
                        .leftProperty()
                            .hasPath("name")
                    .parentCondition()
                        .rightArray()
                            .hasSize(3)
                            .StringAtPosition(0)
                                .hasValue("Boris")
                        .parentArray()
                            .StringAtPosition(1)
                                .hasValue("Donald")
                        .parentArray()
                            .StringAtPosition(2)
                                .hasValue("Jimmy")
        }
    }

    @Test
    fun array_none_of_explicit_three_members(){
        var input = """
            IF name NONE OF Boris, Donald, Jimmy
            THEN unfall
        """

        End2AstRunner.run(input, "{name:\"String\"}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .condition()
                        .hasOperator(ASTComparisonOperator.NONE_OF)
                        .leftProperty()
                            .hasPath("name")
                    .parentCondition()
                        .rightArray()
                            .hasSize(3)
                            .StringAtPosition(0)
                                .hasValue("Boris")
                        .parentArray()
                            .StringAtPosition(1)
                                .hasValue("Donald")
                        .parentArray()
                            .StringAtPosition(2)
                                .hasValue("Jimmy")
        }
    }

    @Test
    fun array_none_of_implicit_three_members(){
        var input = """
            IF name NOT EQUAL Boris, Donald, Jimmy
            THEN unfall
        """

        End2AstRunner.run(input, "{name:\"String\"}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .condition()
                        .hasOperator(ASTComparisonOperator.NONE_OF)
                        .leftProperty()
                            .hasPath("name")
                    .parentCondition()
                        .rightArray()
                            .hasSize(3)
                            .StringAtPosition(0)
                                .hasValue("Boris")
                        .parentArray()
                            .StringAtPosition(1)
                                .hasValue("Donald")
                        .parentArray()
                            .StringAtPosition(2)
                                .hasValue("Jimmy")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
          "IF name EQUAL to Boris, Donald, Jimmy THEN unfall"
        , "IF name EQUAL to Boris, to Donald, to Jimmy THEN unfall"
        , "IF name EQUAL Boris, Donald or Jimmy THEN unfall"
        , "IF name EQUAL to Boris, to Donald or to Jimmy THEN unfall"
        , "IF name is EQUAL to Boris, Donald, Jimmy THEN unfall"
        , "IF name is EQUAL to Boris, to Donald, to Jimmy THEN unfall"
        , "IF name is EQUAL Boris, Donald or Jimmy THEN unfall"
        , "IF name is EQUAL to Boris, to Donald or to Jimmy THEN unfall"
        , "IF the name is EQUAL to Boris, Donald, Jimmy THEN unfall"
        , "IF the name is EQUAL to Boris, to Donald, to Jimmy THEN unfall"
        , "IF the name is EQUAL Boris, Donald or Jimmy THEN unfall"
        , "IF the name is EQUAL to Boris, to Donald or to Jimmy THEN unfall"
    ])
    fun array_at_least_one_of_implicit_as_multiple_expressions(paramString : String)
    {

        var input = paramString

        End2AstRunner.run(input, "{name:\"String\"}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                    .condition()
                        .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
                        .leftProperty()
                            .hasPath("name")
                    .parentCondition()
                        .rightArray()
                            .hasSize(3)
                            .StringAtPosition(0)
                                .hasValue("Boris")
                        .parentArray()
                            .StringAtPosition(1)
                                .hasValue("Donald")
                        .parentArray()
                            .StringAtPosition(2)
                                .hasValue("Jimmy")
        }
    }


    @ParameterizedTest
    @ValueSource(strings = [
        "the name SHOULD NOT be EQUAL to Boris, Donald, Jimmy"
        , "the name MUST NOT be EQUAL to Boris, to Donald, to Jimmy"
        , "the name MUST NOT EQUAL Boris, Donald or Jimmy"
        , "the name MUST NOT be Boris, Donald or Jimmy"
    ])
    fun array_at_least_one_of_implicit_as_multiple_expressions_constrained(paramString : String)
    {

        var input = paramString

        End2AstRunner.run(input, "{name:\"String\"}") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .condition()
                .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
                .leftProperty()
                .hasPath("name")
                .parentCondition()
                .rightArray()
                .hasSize(3)
                .StringAtPosition(0)
                .hasValue("Boris")
                .parentArray()
                .StringAtPosition(1)
                .hasValue("Donald")
                .parentArray()
                .StringAtPosition(2)
                .hasValue("Jimmy")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "Dein Alter muss 3, 4, 5",
        "Dein Alter muss 3, 4 oder 5",
        "Dein Alter muss 3, 4, 5 sein",
        "Dein Alter muss 3, 4 oder 5 sein",
        "Dein Alter muss entweder 3, 4 oder 5 sein",
        "Dein Alter muss gleich 3, 4, 5",
        "Dein Alter muss gleich 3, 4 oder 5",
        "Dein Alter muss gleich 3, 4, 5 sein",
        "Dein Alter muss gleich 3, 4 oder 5 sein"
    ])
    fun DE_array_one_of_array_contains_3_static_numbers_must(paramString : String)
    {

        var input = paramString

        End2AstRunner.run(input, "{Alter: 25}", "de") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .condition()
                .hasOperator(ASTComparisonOperator.NONE_OF)
                .rightArray()
                .hasSize(3)
                .numberAtPosition(3.0, 0)
                .numberAtPosition(4.0, 1)
                .numberAtPosition(5.0, 2)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "Dein Alter darf nicht 3, 4, 5",
        "Dein Alter darf nicht 3, 4 oder 5",
        "Dein Alter darf nicht 3, 4, 5 sein",
        "Dein Alter darf nicht 3, 4 oder 5 sein",
        "Dein Alter darf nicht gleich 3, 4, 5",
        "Dein Alter darf nicht gleich 3, 4 oder 5",
        "Dein Alter darf nicht gleich 3, 4, 5 sein",
        "Dein Alter darf nicht gleich 3, 4 oder 5 sein"
    ])
    fun DE_array_one_of_array_contains_3_static_numbers_must_not(paramString : String)
    {

        var input = paramString

        End2AstRunner.run(input, "{Alter: 25}", "de") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .condition()
                .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
                .rightArray()
                .hasSize(3)
                .numberAtPosition(3.0, 0)
                .numberAtPosition(4.0, 1)
                .numberAtPosition(5.0, 2)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "Dein Alter darf nicht 3, 5",
        "Dein Alter darf nicht 3 oder 5",
        "Dein Alter darf nicht 3, 5 sein",
        "Dein Alter darf nicht 3 oder 5 sein",
        "Dein Alter darf nicht gleich 3, 5",
        "Dein Alter darf nicht gleich 3 oder 5",
        "Dein Alter darf nicht gleich 3, 5 sein",
        "Dein Alter darf nicht gleich 3 oder 5 sein"
    ])
    fun DE_array_one_of_array_contains_2_static_numbers_must_not(paramString : String)
    {

        var input = paramString

        End2AstRunner.run(input, "{Alter: 25}", "de") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .condition()
                .hasOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
                .rightArray()
                .hasSize(2)
                .numberAtPosition(3.0, 0)
                .numberAtPosition(5.0, 1)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "Zahlen als array\n\nDein Alter muss eins von array",
        "Zahlen als array\n\nDein Alter muss eins von dem array",
        "Zahlen als array\n\nDein Alter muss eins von array sein"
    ])
    fun DE_array_one_of_array_var_contains_2_static_numbers_must_not(paramString : String)
    {

        var input = paramString

        End2AstRunner.run(input, "{Alter: 25, Zahlen: [1,2,3]}", "de") {
            r -> r.rules()
                .hasSizeOf(1)
                .first()
                .condition()
                .hasOperator(ASTComparisonOperator.NONE_OF)
                .rightVariable("array")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal)
        }
    }

    //todo lazevedo bool is true OR boolVariable testcase
}