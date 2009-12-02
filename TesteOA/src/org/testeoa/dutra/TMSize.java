package org.testeoa.dutra;

import treemap.TMComputeSize;
import treemap.TMExceptionBadTMNodeKind;
import treemap.TMNode;

public class TMSize implements TMComputeSize {

	@Override
	public float getSize(TMNode arg0) throws TMExceptionBadTMNodeKind {
		// TODO Auto-generated method stub
		return 1.0f;
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
