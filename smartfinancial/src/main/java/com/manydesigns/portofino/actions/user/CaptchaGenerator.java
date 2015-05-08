package com.manydesigns.portofino.actions.user;

import com.github.cage.Cage;
import com.github.cage.image.ConstantColorGenerator;
import com.github.cage.image.EffectConfig;
import com.github.cage.image.Painter;
import com.github.cage.image.ScaleConfig;
import com.github.cage.token.RandomCharacterGeneratorFactory;
import com.github.cage.token.RandomTokenGenerator;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Locale;
import java.util.Random;

public class CaptchaGenerator extends Cage {
	protected static final int HEIGHT = 40;

	protected static final int WIDTH = 200;

	protected static final char[] TOKEN_DEFAULT_CHARACTER_SET = (new String(RandomCharacterGeneratorFactory.DEFAULT_DEFAULT_CHARACTER_SET).replaceAll("b|f|i|j|l|m|o|t", "") + new String(RandomCharacterGeneratorFactory.DEFAULT_DEFAULT_CHARACTER_SET).replaceAll("c|i|o", "").toUpperCase(Locale.ENGLISH) + new String(RandomCharacterGeneratorFactory.ARABIC_NUMERALS).replaceAll("0|1|9", "")).toCharArray();

	protected static final int TOKEN_LEN_MIN = 6;

	protected static final int TOKEN_LEN_DELTA = 2;

	public CaptchaGenerator() {
		this(new Random(), null);
	}

	public CaptchaGenerator(String format) {
		this(new Random(), format);
	}

	protected CaptchaGenerator(Random rnd, @Nullable String format) {
		super(new Painter(WIDTH, HEIGHT, null, Painter.Quality.MAX, new EffectConfig(true, true, false, false, new ScaleConfig(1.0f, 1.0f)), rnd), null, new ConstantColorGenerator(Color.BLACK), format, 1.0f, new RandomTokenGenerator(rnd, new RandomCharacterGeneratorFactory(TOKEN_DEFAULT_CHARACTER_SET, null, rnd), TOKEN_LEN_MIN, TOKEN_LEN_DELTA), rnd);
	}
}