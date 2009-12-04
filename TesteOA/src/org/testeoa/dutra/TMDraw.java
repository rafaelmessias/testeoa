package org.testeoa.dutra;

import java.awt.Color;
import java.awt.Paint;

import treemap.TMComputeDraw;
import treemap.TMExceptionBadTMNodeKind;
import treemap.TMNode;
import treemap.TMNodeAdapter;

public class TMDraw implements TMComputeDraw {

	@Override
	public Paint getFilling(TMNodeAdapter tAdapter) throws TMExceptionBadTMNodeKind {
		if (tAdapter.getNode() instanceof TMTreeNode) {
			TMTreeNode tNode = (TMTreeNode) tAdapter.getNode();
			if (tNode.isMetodo) {
				return Color.RED;
			} else {
				return Color.BLUE;
			}
		} else if (tAdapter.getNode() instanceof TMTreeClass) {
			return Color.GREEN;
		}
		return Color.WHITE;
	}

	@Override
	public String getTitle(TMNodeAdapter tAdapter) throws TMExceptionBadTMNodeKind {
		if (tAdapter.getNode() instanceof TMTreeNode) {
			TMTreeNode tNode = (TMTreeNode) tAdapter.getNode();
			return tNode.getNome();
		} else if (tAdapter.getNode() instanceof TMTreeClass) {
			TMTreeClass tClass = (TMTreeClass) tAdapter.getNode();
			return tClass.getNome();
		} else {
			return "***";
		}
	}

	@Override
	public Paint getTitleColor(TMNodeAdapter arg0)
			throws TMExceptionBadTMNodeKind {
		// TODO Auto-generated method stub
		return Color.BLACK;
	}

	@Override
	public String getTooltip(TMNodeAdapter arg0)
			throws TMExceptionBadTMNodeKind {
		// TODO Auto-generated method stub
		return "*";
	}

	@Override
	public boolean isCompatibleWith(TMNode tNode) {
		if ((tNode instanceof TMTreeNode) || (tNode instanceof TMTreeClass)
				|| (tNode instanceof TMTreePackage)
				|| (tNode instanceof TMTreeRoot)) {
			return true;
		}
		return false;
	}

}
