package br.com.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AtlysFileGroup {

    private List<String> path;
    private String estado;

    @Override
    public String toString() {
        return "AtlysFileGroup{" +
                "path=" + path +
                '}';
    }
}
