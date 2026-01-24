# PR Oluşturma Notları

"Failed to create PR" hatası genellikle uygulama dışı sorunlardan kaynaklanır. Aşağıdaki kontrolleri öneririm:

1. **Uzak depo bağlantısı**: Repo doğru uzak origin ile bağlı mı? `git remote -v` ile kontrol edin.
2. **Kimlik doğrulama**: GitHub/GitLab erişim anahtarı veya OAuth oturumu geçerli mi? Token süresini kontrol edin.
3. **Yetkilendirme**: PR açacağınız depoya yazma/PR oluşturma yetkiniz var mı?
4. **Branch durumu**: PR oluşturmak istediğiniz branch remote'a push edilmiş mi?
5. **CI/PR şablonu**: Zorunlu alanlar veya şablon validasyonları PR oluşturmayı engelliyor olabilir.

Bu adımlardan sonra sorun devam ederse, ilgili platformun hata logs/uyarı mesajlarını paylaşmanız teşhis için yardımcı olur.
