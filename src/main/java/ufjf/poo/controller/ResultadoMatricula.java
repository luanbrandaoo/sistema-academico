package ufjf.poo.controller;

import ufjf.poo.model.Turma;
import ufjf.poo.model.disciplina.Disciplina;

record ResultadoMatricula(Disciplina disciplina, Turma turma, boolean aceita, String motivo) {}