// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordManager
{
    public static final SecurityLogger LOG;
    protected Map<String, PasswordEncryptor> passwordChecker;
    protected PasswordEncryptor defaultPasswordEncryptor;
    protected DatabasePrefixHandler prefixHandler;
    
    public PasswordManager(final PasswordEncryptor defaultPasswordEncryptor, final List<PasswordEncryptor> customPasswordChecker) {
        this.passwordChecker = new HashMap<String, PasswordEncryptor>();
        this.prefixHandler = new DatabasePrefixHandler();
        this.addPasswordCheckerAndThrowErrorIfAlreadyAvailable(new ShaHashDigest());
        this.addPasswordCheckerAndThrowErrorIfAlreadyAvailable(new Sha512HashDigest());
        this.addAllPasswordChecker(customPasswordChecker);
        this.addDefaultEncryptor(defaultPasswordEncryptor);
    }
    
    protected void addAllPasswordChecker(final List<PasswordEncryptor> list) {
        for (final PasswordEncryptor encryptor : list) {
            this.addPasswordCheckerAndThrowErrorIfAlreadyAvailable(encryptor);
        }
    }
    
    protected void addPasswordCheckerAndThrowErrorIfAlreadyAvailable(final PasswordEncryptor encryptor) {
        if (this.passwordChecker.containsKey(encryptor.hashAlgorithmName())) {
            throw PasswordManager.LOG.hashAlgorithmForPasswordEncryptionAlreadyAvailableException(encryptor.hashAlgorithmName());
        }
        this.passwordChecker.put(encryptor.hashAlgorithmName(), encryptor);
    }
    
    protected void addDefaultEncryptor(final PasswordEncryptor defaultPasswordEncryptor) {
        this.defaultPasswordEncryptor = defaultPasswordEncryptor;
        this.passwordChecker.put(defaultPasswordEncryptor.hashAlgorithmName(), defaultPasswordEncryptor);
    }
    
    public String encrypt(final String password) {
        final String prefix = this.prefixHandler.generatePrefix(this.defaultPasswordEncryptor.hashAlgorithmName());
        return prefix + this.defaultPasswordEncryptor.encrypt(password);
    }
    
    public boolean check(final String password, final String encrypted) {
        final PasswordEncryptor encryptor = this.getCorrectEncryptorForPassword(encrypted);
        final String encryptedPasswordWithoutPrefix = this.prefixHandler.removePrefix(encrypted);
        EnsureUtil.ensureNotNull("encryptedPasswordWithoutPrefix", (Object)encryptedPasswordWithoutPrefix);
        return encryptor.check(password, encryptedPasswordWithoutPrefix);
    }
    
    protected PasswordEncryptor getCorrectEncryptorForPassword(final String encryptedPassword) {
        final String hashAlgorithmName = this.prefixHandler.retrieveAlgorithmName(encryptedPassword);
        if (hashAlgorithmName == null || !this.passwordChecker.containsKey(hashAlgorithmName)) {
            throw PasswordManager.LOG.cannotResolveAlgorithmPrefixFromGivenPasswordException(hashAlgorithmName, this.passwordChecker.keySet());
        }
        return this.passwordChecker.get(hashAlgorithmName);
    }
    
    static {
        LOG = ProcessEngineLogger.SECURITY_LOGGER;
    }
}
