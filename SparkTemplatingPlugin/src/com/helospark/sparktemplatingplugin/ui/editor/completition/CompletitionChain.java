package com.helospark.sparktemplatingplugin.ui.editor.completition;

import java.util.List;

import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalRequest;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.domain.CompletitionProposalResponse;

public interface CompletitionChain {

    List<CompletitionProposalResponse> compute(CompletitionProposalRequest request);

}
