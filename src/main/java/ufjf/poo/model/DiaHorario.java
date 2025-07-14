package ufjf.poo.model;

import java.time.LocalTime;
import java.time.DayOfWeek;

public record DiaHorario(DayOfWeek dia, LocalTime hora) {}