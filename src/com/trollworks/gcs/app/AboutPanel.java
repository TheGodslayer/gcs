/*
 * Copyright (c) 1998-2014 by Richard A. Wilkes. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * version 2.0. If a copy of the MPL was not distributed with this file, You
 * can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This Source Code Form is "Incompatible With Secondary Licenses", as defined
 * by the Mozilla Public License, version 2.0.
 */

package com.trollworks.gcs.app;

import com.trollworks.toolkit.annotation.Localize;
import com.trollworks.toolkit.ui.GraphicsUtilities;
import com.trollworks.toolkit.ui.image.ToolkitIcon;
import com.trollworks.toolkit.utility.BundleInfo;
import com.trollworks.toolkit.utility.Localization;
import com.trollworks.toolkit.utility.Version;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.UIManager;

/** The about box contents. */
public class AboutPanel extends JPanel {
	@Localize("Version %s")
	private static String		VERSION_FORMAT;
	@Localize("%s %s\n%s Architecture\nJava %s")
	private static String		PLATFORM_FORMAT;
	@Localize("GURPS is a trademark of Steve Jackson Games, used by permission. All rights reserved.\nThis product includes copyrighted material from the GURPS game, which is used by permission of Steve Jackson Games.\nThe iText Library is licensed under LGPL 2.1 by Bruno Lowagie and Paulo Soares.\nThe Trove Library is licensed under LGPL 2.1 by Eric D. Friedman and Rob Eden.")
	private static String		LICENSES;
	@Localize("Unknown build date")
	private static String		UNKNOWN_BUILD_DATE;
	@Localize("Development Version")
	private static String		DEVELOPMENT;

	static {
		Localization.initialize();
	}

	private static final String	SEPARATOR	= "\n"; //$NON-NLS-1$
	private static final int	HMARGIN		= 4;

	/** Creates a new about panel. */
	public AboutPanel() {
		setOpaque(true);
		setBackground(Color.black);
		ToolkitIcon img = GCSImages.getAbout();
		setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D gc = GraphicsUtilities.prepare(g);
		super.paintComponent(gc);
		GCSImages.getAbout().paintIcon(this, gc, 0, 0);
		RenderingHints saved = (RenderingHints) gc.getRenderingHints().clone();
		gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		gc.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		Font baseFont = UIManager.getFont("TextField.font"); //$NON-NLS-1$
		gc.setFont(baseFont.deriveFont(10f));
		gc.setColor(Color.WHITE);
		int right = getWidth() - HMARGIN;
		int y = draw(gc, LICENSES, getHeight() - HMARGIN, right, true, true);
		BundleInfo bundleInfo = BundleInfo.getDefault();
		long version = bundleInfo.getVersion();
		int y2 = draw(gc, bundleInfo.getCopyrightBanner(), y, right, false, true);
		draw(gc, String.format(PLATFORM_FORMAT, System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"), System.getProperty("java.version")), y, right, false, false);//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		y2 = draw(gc, version != 0 ? Version.toBuildTimestamp(version) : UNKNOWN_BUILD_DATE, y2, right, false, true);
		gc.setFont(baseFont.deriveFont(Font.BOLD, 12f));
		draw(gc, version != 0 ? String.format(VERSION_FORMAT, Version.toString(version, false)) : DEVELOPMENT, y2, right, false, true);
		gc.setRenderingHints(saved);
	}

	private static int draw(Graphics2D gc, String text, int y, int right, boolean addGap, boolean onLeft) {
		String[] one = text.split(SEPARATOR);
		FontMetrics fm = gc.getFontMetrics();
		int fHeight = fm.getAscent() + fm.getDescent();
		for (int i = one.length - 1; i >= 0; i--) {
			gc.drawString(one[i], onLeft ? HMARGIN : right - fm.stringWidth(one[i]), y);
			y -= fHeight;
		}
		if (addGap) {
			y -= fHeight / 2;
		}
		return y;
	}
}