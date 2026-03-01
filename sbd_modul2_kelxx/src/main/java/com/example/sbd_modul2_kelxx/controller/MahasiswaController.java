package com.example.sbd_modul2_kelxx.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.sbd_modul2_kelxx.model.Mahasiswa;

@Controller
public class MahasiswaController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int PAGE_SIZE = 2;

    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1") int page) {
        // Get total count
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM mahasiswa", Integer.class);
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);

        // Clamp page
        if (page < 1) page = 1;
        if (totalPages > 0 && page > totalPages) page = totalPages;

        int offset = (page - 1) * PAGE_SIZE;

        // Oracle 12c+ pagination syntax
        String sql = "SELECT * FROM mahasiswa OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Mahasiswa> mahasiswa = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Mahasiswa.class), offset, PAGE_SIZE);

        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        return "add";
    }

    @PostMapping("/add")
    public String add(Mahasiswa mahasiswa) {
        String sql = "INSERT INTO mahasiswa VALUES(?,?,?,?)";
        jdbcTemplate.update(sql, mahasiswa.getNim(),
                mahasiswa.getNama(), mahasiswa.getAngkatan(), mahasiswa.getGender());
        return "redirect:/";
    }

    @GetMapping("/edit/{nim}")
    public String edit(@PathVariable("nim") String nim, Model model) {
        String sql = "SELECT * FROM mahasiswa WHERE nim = ?";
        Mahasiswa mahasiswa = jdbcTemplate.queryForObject(sql,
                BeanPropertyRowMapper.newInstance(Mahasiswa.class), nim);
        model.addAttribute("mahasiswa", mahasiswa);
        return "edit";
    }

    @PostMapping("/edit")
    public String edit(Mahasiswa mahasiswa) {
        String sql = "UPDATE mahasiswa SET nama = ?, angkatan = ?, gender = ? WHERE nim = ?";
        jdbcTemplate.update(sql, mahasiswa.getNama(), mahasiswa.getAngkatan(), mahasiswa.getGender(),
                mahasiswa.getNim());
        return "redirect:/";
    }

    @GetMapping("/delete/{nim}")
    public String delete(@PathVariable("nim") String nim) {
        String sql = "DELETE FROM mahasiswa WHERE nim = ?";
        jdbcTemplate.update(sql, nim);
        return "redirect:/";
    }
}