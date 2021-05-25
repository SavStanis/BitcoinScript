package com.savstanis.btcs;

public class App
{
    public static void main( String[] args )
    {
        var compiler = new BitcoinScriptCompiler();
        compiler.compile("./examples/opcodes_example.btcs");
    }
}
